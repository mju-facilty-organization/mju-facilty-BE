package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.book.timetable.TimeTable;
import com.example.rentalSystem.domain.book.timetable.TimeTableService;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.domain.facility.implement.FacilityRemover;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.global.cloud.S3Service;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.rentalSystem.domain.facility.importer.util.FacilityNumberNormalizer.normalize;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityJpaRepository facilityJpaRepository;
    private final FacilitySaver facilitySaver;
    private final FacilityImpl facilityImpl;
    private final FacilityRemover facilityRemover;

    private final S3Service s3Service;
    private final TimeTableService timeTableService;

    @Transactional
    public PreSignUrlListResponse create(CreateFacilityRequestDto dto) {
        String normalizedNo = normalize(dto.facilityNumber());

        facilityJpaRepository.findByFacilityNumber(normalizedNo)
                .ifPresent(f -> {
                    throw new CustomException(ErrorType.DUPLICATE_RESOURCE);
                });

        List<String> imageUrlList = (dto.fileNames() == null)
                ? List.of()
                : dto.fileNames().stream().map(s3Service::generateFacilityS3Key).toList();

        List<AffiliationType> boundary = AffiliationType.getChildList(dto.college());

        Facility facility = Facility.builder()
                .facilityType(dto.facilityType())
                .facilityNumber(normalizedNo)
                .images(imageUrlList)
                .capacity(dto.capacity())
                .supportFacilities(dto.supportFacilities())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .allowedBoundary(boundary)
                .isAvailable(dto.isAvailable())
                .build();

        facilitySaver.save(facility);

        List<String> presigned = imageUrlList.stream()
                .map(s3Service::generatePresignedUrlForPut)
                .toList();
        return PreSignUrlListResponse.from(presigned);
    }

    private static String baseName(String name) {
        if (name == null) return null;
        String only = name.replace("\\", "/");
        only = only.substring(only.lastIndexOf('/') + 1); // 경로 제거
        return only.trim().toLowerCase(); // 대소문자/공백 차이 제거
    }

    /**
     * 🔧 update가 이미지 처리 + PUT presign URL 목록을 반환
     */
    @Transactional
    public PreSignUrlListResponse update(UpdateFacilityRequestDto dto, Long facilityId) {
        Facility origin = facilityImpl.findById(facilityId);

        // ===== 1) 메타 갱신 =====
        String newNumber = (dto.facilityNumber() != null) ? normalize(dto.facilityNumber()) : null;

        boolean willChangeNumber = newNumber != null && !newNumber.equals(origin.getFacilityNumber());
        if (willChangeNumber) {
            facilityJpaRepository.findByFacilityNumber(newNumber)
                    .ifPresent(dup -> {
                        if (!dup.getId().equals(origin.getId()))
                            throw new CustomException(ErrorType.DUPLICATE_RESOURCE);
                    });
        }

        FacilityType newType = (dto.facilityType() != null)
                ? FacilityType.getInstanceByValue(dto.facilityType())
                : null;

        origin.updateAll(
                newType,
                newNumber,
                dto.capacity(),
                dto.startTime(),
                dto.endTime(),
                dto.supportFacilities(),
                dto.allowedBoundary(),
                dto.isAvailable()
        );

        // ===== 2) 이미지 삭제/추가/정렬 =====
        List<String> images = new ArrayList<>(origin.getImages() == null ? List.of() : origin.getImages());

        // 2-1) 삭제
        if (dto.removeKeys() != null && !dto.removeKeys().isEmpty()) {
            for (String rk : dto.removeKeys()) {
                if (images.remove(rk) && Boolean.TRUE.equals(dto.hardDelete())) {
                    s3Service.deleteObjectIfExists(rk); // 실패 무시(로그 권장)
                }
            }
        }

        // 2-2) 파일명 중복 방지 준비
        java.util.Set<String> existingNames = new java.util.HashSet<>();
        for (String key : images) {
            String tail = key.substring(key.lastIndexOf('/') + 1); // {UUID}_{original}
            int idx = tail.indexOf('_');
            String original = (idx >= 0) ? tail.substring(idx + 1) : tail; // 원본파일명
            existingNames.add(baseName(original));
        }

        // 2-3) 추가 (중복 파일명 스킵 + presign 발급)
        List<String> presignedPutUrls = new ArrayList<>();
        if (dto.addFileNames() != null) {
            for (String fileName : dto.addFileNames()) {
                String bn = baseName(fileName);
                if (bn == null || bn.isEmpty()) continue;
                if (existingNames.contains(bn)) {
                    // log.info("[facility:{}] Skip duplicate image by filename: {}", origin.getId(), fileName);
                    continue;
                }
                String key = s3Service.generateFacilityS3Key(fileName, origin.getId());
                String putUrl = s3Service.generatePresignedUrlForPut(key);
                presignedPutUrls.add(putUrl);

                images.add(key);
                existingNames.add(bn);
            }
        }

        // 2-4) 정렬
        if (dto.newOrder() != null && !dto.newOrder().isEmpty()) {
            java.util.LinkedHashSet<String> ord = new java.util.LinkedHashSet<>(dto.newOrder());
            List<String> reordered = new ArrayList<>();
            for (String k : images) if (ord.contains(k) && !reordered.contains(k)) reordered.add(k);
            for (String k : images) if (!ord.contains(k) && !reordered.contains(k)) reordered.add(k);
            images = reordered;
        } else {
            images = new ArrayList<>(new java.util.LinkedHashSet<>(images)); // 중복 키 제거
        }

        // 2-5) 엔티티 반영 (NPE 안전)
        origin.replaceImages(images);

        return PreSignUrlListResponse.from(presignedPutUrls);
    }

    @Transactional
    public void delete(Long facilityId) {
        Facility facility = facilityImpl.findById(facilityId);
        facilityRemover.delete(facility);
    }

    @Transactional(readOnly = true)
    public Page<FacilityResponse> getAll(Pageable pageable, String facilityType) {
        Page<Facility> page = (Objects.isNull(facilityType))
                ? facilityJpaRepository.findAll(pageable)
                : facilityJpaRepository.findByFacilityType(FacilityType.getInstanceByValue(facilityType), pageable);

        return page.map(facility -> {
            List<String> presignedUrls = s3Service.generatePresignedUrlsForGet(facility);
            return FacilityResponse.fromFacility(facility, presignedUrls);
        });
    }

    @Transactional(readOnly = true)
    public FacilityDetailResponse getFacilityDetail(Long facilityId, LocalDate localDate) {
        Facility facility = findFacilityById(facilityId);
        TimeTable timeTable = timeTableService.getTimeTable(facility, localDate);
        List<String> presignedUrls = s3Service.generatePresignedUrlsForGet(facility);
        return FacilityDetailResponse.of(facility, timeTable, presignedUrls);
    }

    private Facility findFacilityById(Long id) {
        return facilityJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<TimeTable> getFacilityWeeklySchedule(Long facilityId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) throw new CustomException(ErrorType.INVALID_DATE_RANGE);
        Facility facility = facilityImpl.findById(facilityId);
        return timeTableService.getPeriodTimeTables(facility, startDate, endDate);
    }
}