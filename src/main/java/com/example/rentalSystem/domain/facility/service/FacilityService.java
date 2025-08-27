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

    // allowedBoundary: 선택 학과 우선, 없으면 단과대 하위 전공 전체
    final List<AffiliationType> boundary =
        (dto.allowedBoundary() != null && !dto.allowedBoundary().isEmpty())
            ? mapMajors(dto.allowedBoundary())      // ← 여기
            : AffiliationType.getChildList(dto.college());

    Optional<Facility> existingOpt = facilityJpaRepository.findByFacilityNumber(normalizedNo);

    // 이미 존재 + overwrite=false -> 409
    if (existingOpt.isPresent() && !overwrite) {
      Facility existing = existingOpt.get();
      FacilityDetailResponse payload = FacilityDetailResponse.fromFacilityOnly(
          existing, s3Service.generatePresignedUrlsForGet(existing)
      );
      throw new FacilityConflictException(payload);
    }

    // 이미 존재 + overwrite=true -> update 위임
    if (existingOpt.isPresent()) {
      Facility existing = existingOpt.get();
      UpdateFacilityRequestDto updateDto = new UpdateFacilityRequestDto(
          dto.facilityType(), normalizedNo, dto.supportFacilities(),
          dto.startTime(), dto.endTime(), dto.capacity(), dto.isAvailable(),
          dto.allowedBoundary(), dto.fileNames(),
          existing.getImages() == null ? List.of() : new ArrayList<>(existing.getImages()),
          null, false
      );
      return update(updateDto, existing.getId());
    }

    try {
      // 1) 먼저 엔티티 저장하여 id 확보 (이미지는 일단 빈 배열)
      Facility facility = Facility.builder()
          .facilityType(dto.facilityType())
          .facilityNumber(normalizedNo)
          .images(List.of())
          .capacity(dto.capacity())
          .supportFacilities(dto.supportFacilities())
          .startTime(dto.startTime())
          .endTime(dto.endTime())
          .allowedBoundary(boundary)
          .isAvailable(dto.isAvailable())
          .build();

      facilitySaver.save(facility); // 여기서 facility.getId() 생성

      // 2) fileNames 중복 제거 → facilityId 기반 S3 키 생성
      List<String> imageKeys = (dto.fileNames() == null) ? List.of()
          : dto.fileNames().stream()
              .distinct()
              .map(name -> s3Service.generateFacilityS3Key(name, facility.getId())) // ★ id 포함
              .toList();

      // 3) 엔티티에 이미지 키 세팅 후 저장
      facility.replaceImages(imageKeys);
      facilitySaver.save(facility);

      // 4) 업로드용 presigned PUT URL 반환
      List<String> presigned = imageKeys.stream()
          .map(s3Service::generatePresignedUrlForPut)
          .toList();

      return PreSignUrlListResponse.from(presigned);

    } catch (DataIntegrityViolationException e) {
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

  @Transactional
  public PreSignUrlListResponse update(UpdateFacilityRequestDto dto, Long facilityId) {
    // 0) 대상 조회
    Facility origin = facilityJpaRepository.findById(facilityId)
        .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));

    // 1) 시설번호는 수정 금지 → DTO 값이 와도 무시
    //    (필요하면 여기서 변경 시도 시 예외 던지도록 바꿀 수 있음)
    String keepNumber = origin.getFacilityNumber();

    // 2) 타입 등 메타 필드 덮어쓰기(값이 오면 교체, null이면 유지)
    FacilityType newType = (dto.facilityType() != null)
        ? FacilityType.getInstanceByValue(dto.facilityType())
        : null;

    // allowedBoundary: null이면 유지, 값이 오면 AffiliationType으로 변환해서 교체
    List<AffiliationType> newBoundary = (dto.allowedBoundary() != null)
        ? mapMajors(dto.allowedBoundary())          // ← 여기
        : null;

    origin.updateAll(
        newType,
        null,                         // 번호는 변경 금지 → null 유지
        dto.capacity(),
        dto.startTime(),
        dto.endTime(),
        dto.supportFacilities(),
        newBoundary,                  // null이면 기존 유지
        dto.isAvailable()
    );

    // 3) 이미지 처리: remove → add → 정렬
    List<String> images = new ArrayList<>(origin.getImages() == null ? List.of() : origin.getImages());

    // (1) 삭제
    if (dto.removeKeys() != null && !dto.removeKeys().isEmpty()) {
      for (String rk : dto.removeKeys()) {
        if (images.remove(rk) && Boolean.TRUE.equals(dto.hardDelete())) {
          s3Service.deleteObjectIfExists(rk); // 없으면 조용히 무시
        }
      }
    }

    // (2) 추가: 파일명(원본) 중복 방지
    //     기존 키들에서 "uuid_" 이후의 원본파일명만 뽑아 소문자로 보관
    Set<String> existingNames = new HashSet<>();
    for (String key : images) {
      String tail = key.substring(key.lastIndexOf('/') + 1);
      int idx = tail.indexOf('_');
      String original = (idx >= 0) ? tail.substring(idx + 1) : tail;
      existingNames.add(baseName(original));  // baseName = 소문자 + 경로삭제
    }

    List<String> presignedPutUrls = new ArrayList<>();
    if (dto.addFileNames() != null && !dto.addFileNames().isEmpty()) {
      // 요청 내 중복도 제거
      for (String fileName : dto.addFileNames().stream().distinct().toList()) {
        String bn = baseName(fileName);
        if (bn == null || bn.isEmpty() || existingNames.contains(bn)) {
          continue; // 기존에 같은 원본파일명이 있으면 스킵
        }
        String key = s3Service.generateFacilityS3Key(fileName, origin.getId()); // facilities/{id}/images/{uuid}_{원본}
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
      // 혹시 모를 중복 키 제거(보수적)
      images = new ArrayList<>(new LinkedHashSet<>(images));
    }

    origin.replaceImages(images);

    // 업로드해야 할 것만 presigned 반환
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
    // 1) 엔티티 조회 (없으면 404 예외 발생)
    Facility facility = facilityImpl.findById(facilityId);

    // 2) 이미지 키 백업 (DB 삭제 후에도 S3 키를 써야 하므로 미리 복사)
    List<String> imageKeys = facility.getImages() == null
        ? List.of()
        : List.copyOf(facility.getImages());

    // 3) DB 삭제 (예약 등 FK 제약은 facilityRemover 내부 정책에 따름)
    facilityRemover.delete(facility);

    // 4) S3 삭제
    for (String key : imageKeys) {
      s3Service.deleteObjectIfExists(key); // 없으면 조용히 무시
    }
  }

  @Transactional
  public void deleteAllFacilities() {
    // 1) 모든 시설 로드
    List<Facility> all = facilityJpaRepository.findAll();

    // 2) 모든 이미지 키 수집
    List<String> allKeys = all.stream()
        .flatMap(f -> (f.getImages() == null ? List.<String>of().stream() : f.getImages().stream()))
        .toList();

    // 3) DB 일괄 삭제
    facilityJpaRepository.deleteAllInBatch(); // FK 제약 고려해서 batch delete

    // 4) S3 일괄 삭제
    for (String key : allKeys) {
      s3Service.deleteObjectIfExists(key);
    }
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

  // FacilityService 하단 헬퍼 교체
  private static List<AffiliationType> mapMajors(List<String> majors) {
    if (majors == null) {
      return null;
    }

    List<String> invalid = new ArrayList<>();
    List<AffiliationType> out = new ArrayList<>();

    for (String raw : majors) {
      if (raw == null) {
        continue;
      }
      String v = raw.trim();

      AffiliationType resolved = null;

      // 1) 표시 이름(한글)으로 매칭
      try {
        resolved = AffiliationType.getInstance(v);
      } catch (CustomException ignore) {
        // 2) enum 상수 이름으로 매칭 (대소문자 무시)
        for (AffiliationType t : AffiliationType.values()) {
          if (t.name().equalsIgnoreCase(v)) {
            resolved = t;
            break;
          }
        }
      }

      if (resolved == null) {
        invalid.add(v);
      } else {
        out.add(resolved);
      }
    }

    if (!invalid.isEmpty()) {
      throw new CustomException(
          ErrorType.INVALID_AFFILIATION_TYPE,
          "유효하지 않은 전공: " + String.join(", ", invalid)
      );
    }

    // 중복 제거 + 순서 유지
    return new ArrayList<>(new LinkedHashSet<>(out));
  }
}