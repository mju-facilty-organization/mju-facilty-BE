package com.example.rentalSystem.domain.rentalhistory.service;

import static com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult.PROFESSOR_DENIED;
import static com.example.rentalSystem.domain.rentalhistory.entity.RentalApplicationResult.WAITING;

import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.approval.implement.ProfessorApprovalImpl;
import com.example.rentalSystem.domain.email.service.EmailService;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.domain.professor.ProfessorManager;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalHistoryDetailResponseDto;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rentalhistory.implement.FacilityScheduleManager;
import com.example.rentalSystem.domain.rentalhistory.implement.RentalHistoryImpl;
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
    final ProfessorManager professorManager;
    final ProfessorApprovalImpl professorApprovalImpl;
    final FacilityScheduleManager facilityScheduleManager;
    final EmailService emailService;

    @Transactional
    public void create(Student student, CreateRentalRequest createRentalRequest) {
        Facility facility = facilityImpl.findById(
            Long.parseLong(createRentalRequest.facilityId()));

        facilityScheduleManager.checkAvailabilityAndReserve(
            facility,
            createRentalRequest.startDateTime(),
            createRentalRequest.endDateTime());

        Professor professor = professorManager.findById(createRentalRequest.professorId());
        RentalHistory rentalHistory = createRentalRequest.toEntity(student, facility);

        ProfessorApproval professorApproval = createRentalRequest.toEntity(rentalHistory,
            professor);

        rentalHistoryImpl.save(rentalHistory);
        professorApprovalImpl.save(professorApproval);
        emailService.sendProfessorRentalConfirm(professor.getEmail());
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

    public RentalHistoryDetailResponseDto getRentalHistoryDetailById(Long rentalHistoryId) {
        RentalHistory rentalHistory = rentalHistoryImpl.findById(rentalHistoryId);
        ProfessorApproval professorApproval = professorApprovalImpl.findByRentalHistory(
            rentalHistory);
        Student student = rentalHistory.getStudent();

        if (
            rentalHistory.getRentalApplicationResult() == WAITING ||
                rentalHistory.getRentalApplicationResult() == PROFESSOR_DENIED
        ) {
            return RentalHistoryDetailResponseDto.of(rentalHistory, professorApproval, student);
        }
        return RentalHistoryDetailResponseDto.of(rentalHistory, professorApproval, student,
            rentalHistory.getPic());
    }

    public RentalHistoryResponseDto getRentalHistoryById(Long rentalHistoryId) {
        RentalHistory rentalHistory = rentalHistoryImpl.findById(rentalHistoryId);

        return RentalHistoryResponseDto.from(rentalHistory);
    }
}
