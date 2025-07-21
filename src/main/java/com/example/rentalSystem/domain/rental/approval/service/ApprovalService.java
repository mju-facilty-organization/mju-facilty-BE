package com.example.rentalSystem.domain.rental.approval.service;

import com.example.rentalSystem.domain.rental.approval.dto.request.RegisterRentalResultRequest;
import com.example.rentalSystem.domain.rental.approval.dto.response.RentalHistoryResponseForProfessorDto;
import com.example.rentalSystem.domain.rental.approval.entity.ProfessorApproval;
import com.example.rentalSystem.domain.rental.approval.implement.ProfessorApprovalImpl;
import com.example.rentalSystem.domain.rental.email.implement.MailImpl;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.member.pic.entity.Pic;
import com.example.rentalSystem.domain.member.student.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.rental.rentalhistory.dto.response.RentalInfoResponse;
import com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalApplicationResult;
import com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.rental.rentalhistory.implement.FacilityScheduleManager;
import com.example.rentalSystem.domain.rental.rentalhistory.implement.RentalHistoryImpl;
import com.example.rentalSystem.domain.rental.rentalhistory.implement.RentalHistoryUtils;
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
    final FacilityScheduleManager facilityScheduleManager;

    public void registerRentalResultByProfessor(
        Long professorApprovalId,
        RegisterRentalResultRequest registerRentalResultRequest
    ) {
        ProfessorApproval professorApproval = professorApprovalImpl.findById(professorApprovalId);
        professorApproval.registerResult(registerRentalResultRequest);

        RentalHistory rentalHistory = professorApproval.getRentalHistory();
        updateFacilityTimeStatus(rentalHistory,
            registerRentalResultRequest.rentalApplicationResult());
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

        updateFacilityTimeStatus(rentalHistory,
            registerRentalResultRequest.rentalApplicationResult());
    }


    private RentalInfoResponse createRentalInfo(
        RentalHistory rentalHistory
    ) {
        LocalDateTime startDate = rentalHistory.getRentalStartDateTime();
        LocalDateTime endDate = rentalHistory.getRentalEndDateTime();

        String date = RentalHistoryUtils.formatRentalDate(startDate);
        String time = RentalHistoryUtils.formatRentalTime(startDate, endDate);

        return RentalInfoResponse.of(
            rentalHistory,
            date,
            time
        );
    }


    private void updateFacilityTimeStatus(
        RentalHistory rentalHistory,
        RentalApplicationResult result
    ) {
        Facility facility = rentalHistory.getFacility();
        LocalDateTime rentalStartDateTime = rentalHistory.getRentalStartDateTime();
        LocalDateTime rentalEndDateTime = rentalHistory.getRentalEndDateTime();

        facilityScheduleManager.updateTimeStatus(
            facility,
            rentalStartDateTime,
            rentalEndDateTime,
            result
        );
    }

}
