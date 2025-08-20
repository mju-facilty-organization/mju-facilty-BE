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
import java.util.*;

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

    Optional<Facility> existingOpt = facilityJpaRepository.findByFacilityNumber(normalizedNo);

    if (existingOpt.isPresent()) {
      Facility existing = existingOpt.get();

      // 기존 이미지 전부 제거 + 새 파일만 남도록 강제
      List<String> removeAllExisting =
          (existing.getImages() == null) ? List.of() : new ArrayList<>(existing.getImages());

      UpdateFacilityRequestDto updateDto = new UpdateFacilityRequestDto(
          dto.facilityType(),                               // facilityType (바뀔 수 있음)
          dto.facilityNumber(),                             // facilityNumber (같거나 바뀔 수 있음)
          dto.supportFacilities(),                          // supportFacilities
          dto.startTime(),                                  // startTime
          dto.endTime(),                                    // endTime
          dto.capacity(),                                   // capacity
          dto.isAvailable(),                                // isAvailable
          AffiliationType.getChildList(dto.college()),      // allowedBoundary
          dto.fileNames(),                                  // addFileNames (새로 올릴 파일)
          removeAllExisting,                                // removeKeys (기존 전부 제거 -> 완전 덮어쓰기)
          null,                                             // newOrder
          false                                             // hardDelete (S3 실제 삭제는 선택, 여기선 참조만 제거)
      );
      return update(updateDto, existing.getId());
    }

    // 신규 등록
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
    if (name == null) {
      return null;
    }
    String only = name.replace("\\", "/");
    only = only.substring(only.lastIndexOf('/') + 1);
    return only.trim().toLowerCase();
  }

  // ---------- update ----------
  @Transactional
  public PreSignUrlListResponse update(UpdateFacilityRequestDto dto, Long facilityId) {
    Facility origin = facilityJpaRepository.findById(facilityId)
        .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

    // 1) 번호 변경 감지 + 자기자신 제외 중복 검사
    String reqNumber = dto.facilityNumber();
    String newNumber = (reqNumber != null) ? normalize(reqNumber) : origin.getFacilityNumber(); // null이면 기존 유지
    boolean numberChanged = !newNumber.equals(origin.getFacilityNumber());

    // 중복검사 주석처리
//    if (numberChanged) {
//      boolean exists = facilityJpaRepository.existsByFacilityNumberAndIdNot(newNumber, origin.getId());
//      if (exists) {
//        throw new CustomException(ErrorType.DUPLICATE_RESOURCE);
//      }
//    }

    // 2) 타입 등 메타 필드 덮어쓰기(값이 오면 교체, null이면 유지)
    FacilityType newType = (dto.facilityType() != null)
        ? FacilityType.getInstanceByValue(dto.facilityType())
        : null;

    origin.updateAll(
        newType,
        // 번호는 위에서 newNumber를 이미 결정했으니, 실제로 바뀐 경우만 적용하려면 newNumber 전달,
        // "null이면 유지" 정책을 지키려면 바뀌지 않았다면 null 전달.
        numberChanged ? newNumber : null,
        dto.capacity(),
        dto.startTime(),
        dto.endTime(),
        dto.supportFacilities(),
        dto.allowedBoundary(),
        dto.isAvailable()
    );

    // 3) 이미지 처리: remove → add → 정렬
    List<String> images = new ArrayList<>(origin.getImages() == null ? List.of() : origin.getImages());

    // (1) 삭제: removeKeys 를 보내면 그 키들 제거 (hardDelete=true면 S3도 삭제)
    if (dto.removeKeys() != null && !dto.removeKeys().isEmpty()) {
      for (String rk : dto.removeKeys()) {
        if (images.remove(rk) && Boolean.TRUE.equals(dto.hardDelete())) {
          s3Service.deleteObjectIfExists(rk);
        }
      }
    }

    // (2) 추가: 파일명 중복 방지
    Set<String> existingNames = new HashSet<>();
    for (String key : images) {
      String tail = key.substring(key.lastIndexOf('/') + 1);
      int idx = tail.indexOf('_');
      String original = (idx >= 0) ? tail.substring(idx + 1) : tail;
      existingNames.add(baseName(original));
    }

    List<String> presignedPutUrls = new ArrayList<>();
    if (dto.addFileNames() != null) {
      for (String fileName : dto.addFileNames()) {
        String bn = baseName(fileName);
        if (bn == null || bn.isEmpty() || existingNames.contains(bn)) {
          continue;
        }
        String key = s3Service.generateFacilityS3Key(fileName, origin.getId());
        String putUrl = s3Service.generatePresignedUrlForPut(key);
        presignedPutUrls.add(putUrl);
        images.add(key);
        existingNames.add(bn);
      }
    }

    // (3) 정렬: newOrder가 오면 그 순서로, 없으면 중복 제거만
    if (dto.newOrder() != null && !dto.newOrder().isEmpty()) {
      LinkedHashSet<String> ord = new LinkedHashSet<>(dto.newOrder());
      List<String> reordered = new ArrayList<>();
      for (String k : images) {
        if (ord.contains(k) && !reordered.contains(k)) {
          reordered.add(k);
        }
      }
      for (String k : images) {
        if (!ord.contains(k) && !reordered.contains(k)) {
          reordered.add(k);
        }
      }
      images = reordered;
    } else {
      images = new ArrayList<>(new LinkedHashSet<>(images)); // 중복 제거
    }

    origin.replaceImages(images);
    return PreSignUrlListResponse.from(presignedPutUrls);
  }

  // ---------- getAll (리포지토리의 String 시그니처에 맞춤) ----------
  @Transactional(readOnly = true)
  public Page<FacilityResponse> getAll(Pageable pageable, String facilityType) {
    String typeValue = (facilityType == null)
        ? null
        : FacilityType.getInstanceByValue(facilityType).getValue();

    Page<Facility> page = facilityJpaRepository.findByFacilityType(typeValue, pageable);

    return page.map(facility -> {
      List<String> presignedUrls = s3Service.generatePresignedUrlsForGet(facility);
      return FacilityResponse.fromFacility(facility, presignedUrls);
    });
  }

  // ---------- delete / detail / weekly ----------
  @Transactional
  public void delete(Long facilityId) {
    Facility facility = facilityImpl.findById(facilityId);
    facilityRemover.delete(facility);
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
    if (startDate.isAfter(endDate)) {
      throw new CustomException(ErrorType.INVALID_DATE_RANGE);
    }
    Facility facility = facilityImpl.findById(facilityId);
    return timeTableService.getPeriodTimeTables(facility, startDate, endDate);
  }
}