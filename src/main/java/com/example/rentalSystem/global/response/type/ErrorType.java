package com.example.rentalSystem.global.response.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    BAD_REQUEST(400, "잘못된 요청입니다."),
    ENTITY_NOT_FOUND(400, "요청한 정보로 엔터티를 찾을 수 없습니다."),
    DUPLICATE_EMAIL_RESOURCE(409, "이미 존재하는 이메일입니다."),
    FAIL_AUTHENTICATION(401, "아이디와 비밀번호가 틀렸습니다."),
    EXPIRED_ACCESS_TOKEN(400, "만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(400, "만료된 리프레쉬 토큰입니다."),
    FAIL_SEND_EMAIL(400, "이메일 전송에 실패하였습니다."),
    FAIL_CREATE_AUTH_CODE(400, "인증 코드 생성에 실패하였습니다."),
    EXPIRED_AUTH_TIME(401, "이메일 인증 코드가 만료되었습니다."),
    MISMATCH_VERIFIED_CODE(401, "이메일 인증 코드가 일치하지 않습니다."),
    NOT_VALID_TOKEN(401, "유효한 토큰이 아닙니다."),
    INVALID_REQUEST(400, "형식에 맞지 않는 요청입니다."),
    DUPLICATE_SEND(409, "인증 코드가 발송되어 있습니다."),
    FAIL_RENTAL_REQUEST(409, "예약 신청이 불가능한 시간입니다."),
    INVALID_FACILITY_TYPE(400, "유효한 시설 타입이 아닙니다."),
    INVALID_AFFILIATION_TYPE(400, "유효한 소속이 아닙니다."),
    FAIL_READ_CSV(400, "CSV 파일 읽기를 실패했습니다."),
    FORBIDDEN(403, "해당 요청에 대한 권한이 없습니다."),
    ALREADY_ANSWERED(400, "이미 답변이 작성된 건의입니다."),
    NO_ANSWER_YET(400, "아직 답변이 등록되지 않았습니다."),
    ALREADY_COMPLETED(400, "처리완료 상태에서는 답변을 수정하거나 등록할 수 없습니다."),
    INVALID_TIME_RANGE(400, "시작 시간이 종료 시간보다 빠를 수 없습니다."),
    SCHEDULE_CONFLICT(400, "이미 스케줄이 겹치는 시간이 있습니다."),
    INVALID_DATE_RANGE(400, "유효 날짜가 올바르지 않습니다.");

    private final int httpStatusCode;
    private final String message;
}
