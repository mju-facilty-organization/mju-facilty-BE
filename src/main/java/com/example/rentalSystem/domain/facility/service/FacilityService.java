package com.example.rentalSystem.domain.facility.service;

import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.dto.response.PresignUrlListResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityFinder;
import com.example.rentalSystem.domain.facility.implement.FacilityReader;
import com.example.rentalSystem.domain.facility.implement.FacilityRemover;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
import com.example.rentalSystem.global.cloud.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityService {

    private final FacilityReader facilityReader;
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

    public List<FacilityResponse> getAll() {
        List<Facility> facilities = facilityReader.getAll();

        return facilities.stream()
            .map(FacilityResponse::fromFacility)
            .toList();
    }
}
