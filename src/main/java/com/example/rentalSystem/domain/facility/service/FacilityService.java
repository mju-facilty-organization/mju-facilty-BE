package com.example.rentalSystem.domain.facility.service;

import static com.example.rentalSystem.domain.facility.importer.util.FacilityNumberNormalizer.normalize;

import com.example.rentalSystem.domain.book.timetable.TimeTable;
import com.example.rentalSystem.domain.book.timetable.TimeTableService;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.facility.exception.FacilityConflictException;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.domain.facility.implement.FacilityRemover;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.global.cloud.S3Service;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacilityService {

  private final FacilityJpaRepository facilityJpaRepository;
  private final FacilitySaver facilitySaver;
  private final FacilityImpl facilityImpl;
  private final FacilityRemover facilityRemover;

  private final S3Service s3Service;
  private final TimeTableService timeTableService;

  /**
   * 단일 시설 등록 - facilityNumber는 normalize하여 키로 사용 - overwrite=false 이고 번호 충돌 시 409 + 기존 시설 스냅샷(payload) 포함 -
   * overwrite=true 이고 번호 충돌 시 덮어쓰기(update 흐름으로 위임) - 신규 생성 시: 먼저 엔티티 저장해 id 확보 → id 기반 S3 key 생성 → presign 발급 → DB 이미지
   * 세팅 - 요청 파일명은 baseName 정규화 후 중복 제거
   */

  @Transactional
  public PreSignUrlListResponse create(CreateFacilityRequestDto dto, boolean overwrite) {
    final String normalizedNo = normalize(dto.facilityNumber());

    // 0) allowedBoundary 우선 사용, 없으면 college 전체 전공으로 대체
    final List<AffiliationType> boundary =
        (dto.allowedBoundary() != null && !dto.allowedBoundary().isEmpty())
            ? dto.allowedBoundary().stream()
            .map(AffiliationType::getInstance) // "행정학과" -> AffiliationType
            .toList()
            : AffiliationType.getChildList(dto.college()); // 단과대학 하위 전공 전체

    Optional<Facility> existingOpt = facilityJpaRepository.findByFacilityNumber(normalizedNo);

    // 1) 이미 존재 + overwrite=false → 409 with 요약 payload
    if (existingOpt.isPresent() && !overwrite) {
      Facility existing = existingOpt.get();
      FacilityDetailResponse payload = FacilityDetailResponse.fromFacilityOnly(
          existing,
          s3Service.generatePresignedUrlsForGet(existing)
      );
      throw new FacilityConflictException(payload);
    }

    // 2) 이미 존재 + overwrite=true → 덮어쓰기(update 흐름으로 위임)
    if (existingOpt.isPresent()) {
      Facility existing = existingOpt.get();
      UpdateFacilityRequestDto updateDto = new UpdateFacilityRequestDto(
          dto.facilityType(),
          normalizedNo,
          dto.supportFacilities(),
          dto.startTime(),
          dto.endTime(),
          dto.capacity(),
          dto.isAvailable(),
          boundary,                                 // ★ 선택 전공 반영
          dto.fileNames(),
          (existing.getImages() == null) ? List.of() : new ArrayList<>(existing.getImages()),
          null,
          false
      );
      return update(updateDto, existing.getId());
    }

    // 3) 신규 생성
    try {
      // 같은 파일명을 중복으로 보내도 presigned 1개만 발급
      List<String> imageKeys = (dto.fileNames() == null)
          ? List.of()
          : dto.fileNames().stream()
              .distinct()
              .map(s3Service::generateFacilityS3Key) // UUID_prefix + 원본파일명
              .toList();

      Facility facility = Facility.builder()
          .facilityType(dto.facilityType())
          .facilityNumber(normalizedNo)
          .images(imageKeys)
          .capacity(dto.capacity())
          .supportFacilities(dto.supportFacilities())
          .startTime(dto.startTime())
          .endTime(dto.endTime())
          .allowedBoundary(boundary)                // ★ 선택 전공 반영
          .isAvailable(dto.isAvailable())
          .build();

      facilitySaver.save(facility);

      List<String> presigned = imageKeys.stream()
          .map(s3Service::generatePresignedUrlForPut)
          .toList();

      return PreSignUrlListResponse.from(presigned);

    } catch (DataIntegrityViolationException e) {
      // 동시 등록으로 인한 DB 유니크 제약 충돌
      throw new CustomException(ErrorType.DUPLICATE_RESOURCE);
    }
  }

  /**
   * 경로 제거 + 파일명만 소문자화(중복 비교용)
   */
  private static String baseName(String name) {
    if (name == null) {
      return null;
    }
    String only = name.replace("\\", "/");
    only = only.substring(only.lastIndexOf('/') + 1);
    return only.trim().toLowerCase();
  }

  // ---------- update (기존 로직 유지) ----------
  @Transactional
  public PreSignUrlListResponse update(UpdateFacilityRequestDto dto, Long facilityId) {
    Facility origin = facilityJpaRepository.findById(facilityId)
        .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

    // 1) 번호 변경 감지 (현재 정책상 번호 변경은 사실상 금지 흐름이지만, 기존 로직 존중)
    String reqNumber = dto.facilityNumber();
    String newNumber = (reqNumber != null) ? normalize(reqNumber) : origin.getFacilityNumber();
    boolean numberChanged = !newNumber.equals(origin.getFacilityNumber());

    // 2) 타입 등 메타 필드 덮어쓰기(값이 오면 교체, null이면 유지)
    FacilityType newType = (dto.facilityType() != null)
        ? FacilityType.getInstanceByValue(dto.facilityType())
        : null;

    origin.updateAll(
        newType,
        numberChanged ? newNumber : null,          // null이면 유지
        dto.capacity(),
        dto.startTime(),
        dto.endTime(),
        dto.supportFacilities(),
        dto.allowedBoundary(),
        dto.isAvailable()
    );

    // 3) 이미지 처리: remove → add → 정렬
    List<String> images = new ArrayList<>(origin.getImages() == null ? List.of() : origin.getImages());

    // (1) 삭제
    if (dto.removeKeys() != null && !dto.removeKeys().isEmpty()) {
      for (String rk : dto.removeKeys()) {
        if (images.remove(rk) && Boolean.TRUE.equals(dto.hardDelete())) {
          s3Service.deleteObjectIfExists(rk);
        }
      }
    }

    // (2) 추가: 파일명 중복 방지(원본파일명 기준)
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

    // (3) 정렬
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

  // ---------- getAll ----------
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

  // ---------- delete ----------
  @Transactional
  public void delete(Long facilityId) {
    Facility facility = facilityImpl.findById(facilityId);
    facilityRemover.delete(facility);
  }

  // ---------- detail ----------
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

  // ---------- weekly ----------
  @Transactional(readOnly = true)
  public List<TimeTable> getFacilityWeeklySchedule(Long facilityId, LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      throw new CustomException(ErrorType.INVALID_DATE_RANGE);
    }
    Facility facility = facilityImpl.findById(facilityId);
    return timeTableService.getPeriodTimeTables(facility, startDate, endDate);
  }
}