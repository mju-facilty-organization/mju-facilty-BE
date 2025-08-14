package com.example.rentalSystem.domain.book.rentalhistory.implement;

import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.book.rentalhistory.entity.type.RentalApplicationResult;
import com.example.rentalSystem.domain.book.rentalhistory.repository.RentalHistoryRepository;
import com.example.rentalSystem.domain.member.student.entity.Student;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RentalHistoryImpl {

    private final RentalHistoryRepository rentalHistoryRepository;

    public RentalHistory findById(Long id) {
        return rentalHistoryRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public RentalHistory save(RentalHistory rentalHistory) {
        return rentalHistoryRepository.save(rentalHistory);
    }

    public Page<RentalHistory> findAll(Pageable pageable) {
        return rentalHistoryRepository.findAll(pageable);
    }

    public List<RentalHistory> findAllByStudent(Student student) {
        return rentalHistoryRepository.findAllByStudent(student);
    }

    public List<RentalHistory> getByFacilityIdAndDateAndBetweenTime(
            Long facilityId, LocalDate startDate,
            LocalTime startTime, LocalTime endTime
    ) {
        return rentalHistoryRepository.findByFacilityIdAndDateAndBetweenTime(facilityId, startDate,
                startTime, endTime);
    }

    public List<RentalHistory> getByFacilityIdAndDate(Long facilityId, LocalDate localDate) {
        return rentalHistoryRepository.findByFacilityIdAndDate(facilityId, localDate);
    }

    public List<RentalHistory> findCurrentlyInUseByFacility(Long facilityId, LocalDate today, LocalTime nowTime) {
        return rentalHistoryRepository.findCurrentlyInUseByFacility(
                facilityId, today, nowTime, RentalApplicationResult.PIC_PERMITTED
        );
    }
}

