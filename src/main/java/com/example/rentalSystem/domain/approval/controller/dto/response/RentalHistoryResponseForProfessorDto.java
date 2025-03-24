package com.example.rentalSystem.domain.approval.controller.dto.response;

import com.example.rentalSystem.domain.rentalhistory.controller.dto.response.RentalPlaceInfoResponse;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentInfoResponse;

public record RentalHistoryResponseForProfessorDto(
            /*
        학번, 이름, 이메일, 소속 학과, 소속 단체, 휴대폰 번호, 사유, 경고 횟수
        공간명, 시설 타입, 예약일시 예약 시간, 인원수
        */
    Long professorApprovalId,
    StudentInfoResponse studentInfoResponse,
    RentalPlaceInfoResponse rentalPlaceInfoResponse

) {

    public static RentalHistoryResponseForProfessorDto of(
        Long professorApprovalId,
        RentalPlaceInfoResponse rentalPlaceInfoResponse,
        StudentInfoResponse studentInfoResponse) {
        return new RentalHistoryResponseForProfessorDto(
            professorApprovalId,
            studentInfoResponse,
            rentalPlaceInfoResponse
        );
    }
}
