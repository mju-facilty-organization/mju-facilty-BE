package com.example.rentalSystem.domain.rentalhistory.service;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityFinder;
import com.example.rentalSystem.domain.rentalhistory.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rentalhistory.implement.RentalScheduler;
import com.example.rentalSystem.domain.rentalhistory.repository.RentalHistoryRepository;
import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.domain.student.implement.StudentFinder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RentalService {

    final RentalHistoryRepository rentalHistoryRepository;
    final FacilityFinder facilityFinder;
    final RentalScheduler rentalScheduler;
    final StudentFinder studentFinder;


    @Transactional
    public void create(Student student, CreateRentalRequest createRentalRequest) {
        Facility facility = facilityFinder.findById(
            Long.parseLong(createRentalRequest.facilityId()));

        rentalScheduler.checkSchedule(
            facility,
            createRentalRequest.startTime(),
            createRentalRequest.endTime());

        RentalHistory rentalHistory = createRentalRequest.toEntity(student, facility);
        rentalHistoryRepository.save(rentalHistory);
    }

    public Page<RentalHistoryResponseDto> getAllRentalHistory(Pageable pageable) {
        Page<RentalHistory> rentalHistories = rentalHistoryRepository.findAll(pageable);
        return rentalHistories
            .map(RentalHistoryResponseDto::from);
    }

    public List<RentalHistoryResponseDto> getAllRentalHistoryByStudentId(String studentId) {
        Student student = studentFinder.findById(studentId);
        List<RentalHistory> rentalHistories = rentalHistoryRepository.findAllByStudent(
            student);
        return rentalHistories.stream()
            .map(RentalHistoryResponseDto::from)
            .toList();
    }
}
