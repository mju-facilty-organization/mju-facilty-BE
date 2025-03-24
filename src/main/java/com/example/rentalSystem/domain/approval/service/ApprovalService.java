package com.example.rentalSystem.domain.approval.service;

import com.example.rentalSystem.domain.approval.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.approval.controller.dto.response.RentalHistoryResponseForProfessorDto;
import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.approval.implement.ProfessorApprovalImpl;
import com.example.rentalSystem.domain.email.implement.MailImpl;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalPlaceInfoResponse;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.student.entity.Student;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApprovalService {

    final ProfessorApprovalImpl professorApprovalImpl;
    final MailImpl mailImpl;

    public void registerRentalResult(
        Long professorApprovalId,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        ProfessorApproval professorApproval = professorApprovalImpl.findById(professorApprovalId);
        professorApproval.registerResult(registerRentalResultRequest);
    }

    public RentalHistoryResponseForProfessorDto getRentalHistoryInfo(Long professorApprovalId) {
        ProfessorApproval professorApproval = professorApprovalImpl.findById(professorApprovalId);

        RentalHistory rentalHistory = professorApproval.getRentalHistory();
        RentalHistoryResponseDto detailResponseDto = RentalHistoryResponseDto.toDetailResponseDto(
            rentalHistory, professorApproval);

        RentalPlaceInfoResponse rentalPlaceInfoResponse = createRentalPlaceInfo(detailResponseDto);

        Student student = rentalHistory.getStudent();
        StudentInfoResponse studentInfoResponse = StudentInfoResponse.toStudentInfoResponse(
            student);

        return RentalHistoryResponseForProfessorDto.of(professorApprovalId,
            rentalPlaceInfoResponse,
            studentInfoResponse);

    }

    private RentalPlaceInfoResponse createRentalPlaceInfo(
        RentalHistoryResponseDto rentalHistoryResponseDto
    ) {

        LocalDate localDate = rentalHistoryResponseDto.getStartTime().toLocalDate();
        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String startTime = rentalHistoryResponseDto.getStartTime().format(dateTimeFormatter);
        String endTime = rentalHistoryResponseDto.getEndTime().format(dateTimeFormatter);
        String time = startTime + " ~ " + endTime;

        return RentalPlaceInfoResponse.of(
            rentalHistoryResponseDto,
            date,
            time
        );
    }
}
