package com.example.rentalSystem.domain.rentalhistory.service;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.domain.professor.ProfessorImpl;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.rentalhistory.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.dto.response.RentalHistoryDetailResponseDto;
import com.example.rentalSystem.domain.rentalhistory.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rentalhistory.entity.ProfessorHistory;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rentalhistory.implement.ProfessorHistoryImpl;
import com.example.rentalSystem.domain.rentalhistory.implement.RentalHistoryImpl;
import com.example.rentalSystem.domain.rentalhistory.implement.RentalScheduler;
import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.domain.student.implement.StudentImpl;
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

    final RentalHistoryImpl rentalHistoryImpl;
    final FacilityImpl facilityImpl;
    final StudentImpl studentImpl;
    final ProfessorImpl professorImpl;
    final ProfessorHistoryImpl professorHistoryImpl;
    final RentalScheduler rentalScheduler;

    @Transactional
    public void create(Student student, CreateRentalRequest createRentalRequest) {
        Facility facility = facilityImpl.findById(
            Long.parseLong(createRentalRequest.facilityId()));

        rentalScheduler.checkSchedule(
            facility,
            createRentalRequest.startTime(),
            createRentalRequest.endTime());

        Professor professor = professorImpl.findById(createRentalRequest.professorId());
        RentalHistory rentalHistory = createRentalRequest.toEntity(student, facility);

        ProfessorHistory professorHistory = createRentalRequest.toEntity(rentalHistory, professor);
        rentalHistoryImpl.save(rentalHistory);
        professorHistoryImpl.save(professorHistory);
    }

    public Page<RentalHistoryResponseDto> getAllRentalHistory(Pageable pageable) {
        Page<RentalHistory> page = rentalHistoryImpl.findAll(pageable);
        return page.map(RentalHistoryResponseDto::from);
    }

    public List<RentalHistoryResponseDto> getAllRentalHistoryByStudentId(String studentId) {
        Student student = studentImpl.findById(studentId);
        List<RentalHistory> rentalHistories = rentalHistoryImpl.findAllByStudent(student);
        return rentalHistories.stream()
            .map(RentalHistoryResponseDto::from)
            .toList();
    }

    public RentalHistoryDetailResponseDto getRentalHistoryById(String rentalHistoryId) {
        RentalHistory rentalHistory = rentalHistoryImpl.findById(rentalHistoryId);
        ProfessorHistory professorHistory = professorHistoryImpl.findByRentalHistory(rentalHistory);
        Student student = rentalHistory.getStudent();
        return RentalHistoryDetailResponseDto.of(rentalHistory, professorHistory, student);
    }
}
