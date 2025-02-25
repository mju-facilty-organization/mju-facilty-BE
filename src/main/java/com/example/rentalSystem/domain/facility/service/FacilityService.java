package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.facility.controller.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.controller.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityDetailResponse;
import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.controller.dto.response.PreSignUrlListResponse;
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

        // 이미지 이름을 s3 url로 변환
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

        return page.map(FacilityResponse::fromFacility);
    }

    public FacilityDetailResponse getFacilityDetail(Long facilityId, LocalDate localDate) {
        Facility facility = findFacilityById(facilityId);
        TimeTable timeTable = findTimeTableByFacilityAndDate(facility, localDate);
        return FacilityDetailResponse.of(facility, timeTable);
    }

    /**
     * Retrieves the Facility corresponding to the given id.
     *
     * <p>This method attempts to fetch a Facility from the repository using the provided id.
     * If the Facility is not found, it throws a CustomException with an error type of ENTITY_NOT_FOUND.
     *
     * @param id the unique identifier of the Facility
     * @return the Facility matching the provided id
     * @throws CustomException if no Facility is found with the specified id
     */
    private Facility findFacilityById(Long id) {
        return facilityJpaRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }

    /**
     * Retrieves the timetable for the specified facility on a given date.
     *
     * <p>This method searches for a timetable that matches the provided facility and date.
     * If no timetable is found, it throws a {@link CustomException} with an error type of
     * {@code ENTITY_NOT_FOUND}.</p>
     *
     * @param facility the facility for which the timetable is being retrieved
     * @param date the date corresponding to the timetable
     * @return the matching TimeTable
     * @throws CustomException if a timetable for the facility on the specified date does not exist
     */
    private TimeTable findTimeTableByFacilityAndDate(Facility facility, LocalDate date) {
        return timeTableRepository.findByFacilityAndDate(facility, date)
            .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND));
    }
}
