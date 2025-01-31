package com.example.rentalSystem.domain.rentalhistory.service;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityFinder;
import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.rentalhistory.dto.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rentalhistory.implement.RentalScheduler;
import com.example.rentalSystem.domain.rentalhistory.repository.RentalHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RentalService {

    final RentalHistoryRepository rentalHistoryRepository;
    final FacilityFinder facilityFinder;
    final RentalScheduler rentalScheduler;

    @Transactional
    public void create(Member member, CreateRentalRequest createRentalRequest) {
        Facility facility = facilityFinder.findById(
            Long.parseLong(createRentalRequest.facilityId()));

        rentalScheduler.checkSchedule(
            facility,
            createRentalRequest.startTime(),
            createRentalRequest.endTime());

        RentalHistory rentalHistory = createRentalRequest.toEntity(member, facility);
        rentalHistoryRepository.save(rentalHistory);
    }
}
