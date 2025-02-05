package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PresignUrlListResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.FacilityType;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeTable;
import com.example.rentalSystem.domain.facility.implement.FacilityFinder;
import com.example.rentalSystem.domain.facility.implement.FacilityRemover;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.domain.facility.reposiotry.TimeTableRepository;
import com.example.rentalSystem.global.cloud.S3Service;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityService {

    private final FacilityJpaRepository facilityJpaRepository;
    private final TimeTableRepository timeTableRepository;
    private final FacilitySaver facilitySaver;
    private final FacilityFinder facilityFinder;
    private final FacilityRemover facilityRemover;

    private final S3Service s3Service;

    @Transactional
    public PresignUrlListResponse create(CreateFacilityRequestDto createFacilityRequestDto) {

        // 이미지 이름을 s3 url로 변환
        List<String> imageUrlList =
            createFacilityRequestDto
                .fileNames()
                .stream()
                .map(s3Service::generateFacilityS3Key)
                .toList();

        Facility facility = createFacilityRequestDto.toFacility(imageUrlList);
        facilitySaver.save(facility);

        // 시작시간과 끝시간을 이용한 타임 테이블 생성
        TimeTable timeTable = TimeTable.toEntity(
            facility,
            LocalDate.now(),
            createFacilityRequestDto.startTime(),
            createFacilityRequestDto.endTime());
        timeTableRepository.save(timeTable);

        // pre signed url을 반환. 업로드용
        List<String> presignedUrlList = imageUrlList
            .stream()
            .map(s3Service::generatePresignedUrl)
            .toList();
        return PresignUrlListResponse.from(presignedUrlList);
    }


    @Transactional
    public void update(UpdateFacilityRequestDto requestDto, Long facilityId) {
        Facility originFacility = facilityFinder.findById(facilityId);
        Facility updateFacility = requestDto.toFacility();
        originFacility.update(updateFacility);
    }

    @Transactional
    public void delete(Long facilityId) {
        Facility facility = facilityFinder.findById(facilityId);
        facilityRemover.delete(facility);
    }

    public Page<FacilityResponse> getAll(Pageable pageable, String facilityType) {
        Page<Facility> facilities;
        if (Objects.isNull(facilityType)) {
            facilities = facilityJpaRepository.findAll(pageable);
        } else {
            facilities = facilityJpaRepository.findByFacilityType(
                FacilityType.getInstanceByValue(facilityType),
                pageable);
        }
        return facilities
            .map(FacilityResponse::fromFacility);
    }

    public FacilityDetailResponse getFacilityDetail(Long facilityId, LocalDate localDate) {
        Facility facility = findFacilityById(facilityId);
        TimeTable timeTable = findTimeTableByFacilityAndDate(facility, localDate);
        return FacilityDetailResponse.of(facility, timeTable);
    }

    private Facility findFacilityById(Long id) {
        return facilityJpaRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    private TimeTable findTimeTableByFacilityAndDate(Facility facility, LocalDate date) {
        return timeTableRepository.findByFacilityAndDate(facility, date)
            .orElseThrow(EntityNotFoundException::new);
    }
}
