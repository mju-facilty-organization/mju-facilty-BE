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
import java.util.List;
import java.util.Objects;

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
    public PreSignUrlListResponse create(CreateFacilityRequestDto createFacilityRequestDto) {
        List<String> imageUrlList =
                createFacilityRequestDto
                        .fileNames()
                        .stream()
                        .map(s3Service::generateFacilityS3Key)
                        .toList();

        List<AffiliationType> affiliationTypes = AffiliationType.getChildList(
                createFacilityRequestDto.college()
        );

        Facility facility = createFacilityRequestDto.toFacility(imageUrlList, affiliationTypes);
        facilitySaver.save(facility);

        List<String> presignedUrlList = imageUrlList
                .stream()
                .map(s3Service::generatePresignedUrlForPut)
                .toList();
        return PreSignUrlListResponse.from(presignedUrlList);
    }


    @Transactional
    public void update(UpdateFacilityRequestDto requestDto, Long facilityId) {
        Facility originFacility = facilityImpl.findById(facilityId);
        Facility updateFacility = requestDto.toFacility();
        originFacility.update(updateFacility);
    }

    @Transactional
    public void delete(Long facilityId) {
        Facility facility = facilityImpl.findById(facilityId);
        facilityRemover.delete(facility);
    }

    @Transactional(readOnly = true)
    public Page<FacilityResponse> getAll(Pageable pageable, String facilityType) {
        Page<Facility> page;
        if (Objects.isNull(facilityType)) {
            page = facilityJpaRepository.findAll(pageable);
        } else {
            page = facilityJpaRepository.findByFacilityType(
                    FacilityType.getInstanceByValue(facilityType),
                    pageable);
        }
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
    public List<TimeTable> getFacilityWeeklySchedule(
            Long facilityId, LocalDate startDate, LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            throw new CustomException(ErrorType.INVALID_DATE_RANGE);
        }
        Facility facility = facilityImpl.findById(facilityId);
        return timeTableService.getPeriodTimeTables(facility,
                startDate, endDate);
    }

}
