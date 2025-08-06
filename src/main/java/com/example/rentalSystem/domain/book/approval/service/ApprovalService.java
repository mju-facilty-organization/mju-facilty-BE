package com.example.rentalSystem.domain.book.approval.service;

import com.example.rentalSystem.domain.book.approval.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.book.approval.dto.response.RentalHistoryResponseForProfessorDto;
import com.example.rentalSystem.domain.book.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.book.approval.implement.ProfessorApprovalImpl;
import com.example.rentalSystem.domain.book.rentalhistory.dto.response.RentalInfoResponse;
import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.book.rentalhistory.implement.FacilityScheduleManager;
import com.example.rentalSystem.domain.book.rentalhistory.implement.RentalHistoryImpl;
import com.example.rentalSystem.domain.book.rentalhistory.implement.RentalHistoryUtils;
import com.example.rentalSystem.domain.email.implement.MailImpl;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.member.pic.entity.Pic;
import com.example.rentalSystem.domain.member.student.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.member.student.entity.Student;
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
    final FacilityScheduleManager facilityScheduleManager;

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

    public void registerRentalResultByPic(
        Long rentalHistoryId,
        Pic pic,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        RentalHistory rentalHistory = rentalHistoryImpl.findById(rentalHistoryId);
        rentalHistory.registerApplicationResult(pic, registerRentalResultRequest);
    }


    private RentalInfoResponse createRentalInfo(
        RentalHistory rentalHistory
    ) {
        String date = RentalHistoryUtils.formatRentalDate(rentalHistory.getRentalStartDate());
        String time = RentalHistoryUtils.formatRentalTime(rentalHistory.getRentalStartTime(),
            rentalHistory.getRentalEndTime());
        return RentalInfoResponse.of(
            rentalHistory,
            date,
            time
        );
    }
}
