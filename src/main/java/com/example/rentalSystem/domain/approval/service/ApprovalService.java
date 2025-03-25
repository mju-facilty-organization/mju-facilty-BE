package com.example.rentalSystem.domain.approval.service;

import com.example.rentalSystem.domain.approval.controller.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.approval.controller.dto.response.RentalHistoryResponseForProfessorDto;
import com.example.rentalSystem.domain.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.approval.implement.ProfessorApprovalImpl;
import com.example.rentalSystem.domain.email.implement.MailImpl;
import com.example.rentalSystem.domain.facility.controller.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.pic.entity.Pic;
import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalInfoResponse;
import com.example.rentalSystem.domain.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rentalhistory.implement.RentalHistoryImpl;
import com.example.rentalSystem.domain.rentalhistory.implement.RentalHistoryUtils;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.student.entity.Student;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApprovalService {

    final ProfessorApprovalImpl professorApprovalImpl;
    final RentalHistoryImpl rentalHistoryImpl;
    final MailImpl mailImpl;

    public void registerRentalResultByProfessor(
        Long professorApprovalId,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        ProfessorApproval professorApproval = professorApprovalImpl.findById(professorApprovalId);
        professorApproval.registerResult(registerRentalResultRequest);
    }

    @Transactional(readOnly = true)
    public RentalHistoryResponseForProfessorDto getRentalHistoryInfo(Long professorApprovalId) {
        ProfessorApproval professorApproval = professorApprovalImpl.findById(professorApprovalId);

        RentalHistory rentalHistory = professorApproval.getRentalHistory();
        RentalInfoResponse rentalInfoResponse = createRentalInfo(rentalHistory);

        Student student = rentalHistory.getStudent();
        StudentInfoResponse studentInfoResponse = StudentInfoResponse.toStudentInfoResponse(
            student);

        FacilityResponse facilityResponse = FacilityResponse.fromRentalHistory(rentalHistory);

        return RentalHistoryResponseForProfessorDto.of(
            professorApprovalId,
            rentalInfoResponse,
            studentInfoResponse,
            facilityResponse
        );

    }

    private RentalInfoResponse createRentalInfo(
        RentalHistory rentalHistory
    ) {
        LocalDateTime startDate = rentalHistory.getRentalStartDate();
        LocalDateTime endDate = rentalHistory.getRentalEndDate();

        String date = RentalHistoryUtils.formatRentalDate(startDate);
        String time = RentalHistoryUtils.formatRentalTime(startDate, endDate);

        return RentalInfoResponse.of(
            rentalHistory,
            date,
            time
        );
    }

    public void registerRentalResultByPic(
        Long rentalHistoryId,
        Pic pic,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        RentalHistory rentalHistory = rentalHistoryImpl.findById(rentalHistoryId);
        rentalHistory.defineApplicationResult(pic, registerRentalResultRequest);

    }
}
