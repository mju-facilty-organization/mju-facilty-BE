package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.FacilityType;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeTable;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.domain.facility.implement.FacilityRemover;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.domain.facility.reposiotry.TimeTableRepository;
import com.example.rentalSystem.global.cloud.S3Service;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
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
    private final FacilityImpl facilityImpl;
    private final FacilityRemover facilityRemover;

    private final S3Service s3Service;

    @Transactional
    public PreSignUrlListResponse create(CreateFacilityRequestDto createFacilityRequestDto) {
        List<String> imageUrlList =
            createFacilityRequestDto
                .fileNames()
                .stream()
                .map(s3Service::generateFacilityS3Key)
                .toList();

        List<AffiliationType> affiliationTypes = AffiliationType.getChildList(
            createFacilityRequestDto.college());

        Facility facility = createFacilityRequestDto.toFacility(imageUrlList, affiliationTypes);
        facilitySaver.save(facility);

        TimeTable timeTable = TimeTable.toEntity(
            facility,
            LocalDate.now(),
            createFacilityRequestDto.startTime(),
            createFacilityRequestDto.endTime());
        timeTableRepository.save(timeTable);

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

    public FacilityDetailResponse getFacilityDetail(Long facilityId, LocalDate localDate) {
        Facility facility = findFacilityById(facilityId);
        TimeTable timeTable = findTimeTableByFacilityAndDate(facility, localDate);
        List<String> presignedUrls = s3Service.generatePresignedUrlsForGet(facility);
        return FacilityDetailResponse.of(facility, timeTable, presignedUrls);
    }

    private Facility findFacilityById(Long id) {
        return facilityJpaRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    private TimeTable findTimeTableByFacilityAndDate(Facility facility, LocalDate date) {
        return timeTableRepository.findByFacilityAndDate(facility, date)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }
}
