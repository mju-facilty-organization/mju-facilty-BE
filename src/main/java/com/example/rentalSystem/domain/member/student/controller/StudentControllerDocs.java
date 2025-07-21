package com.example.rentalSystem.domain.member.student.controller;

import static com.example.rentalSystem.global.response.type.ErrorType.DUPLICATE_EMAIL_RESOURCE;
import static com.example.rentalSystem.global.response.type.ErrorType.ENTITY_NOT_FOUND;
import static com.example.rentalSystem.global.response.type.ErrorType.FAIL_AUTHENTICATION;
import static com.example.rentalSystem.global.response.type.ErrorType.INVALID_AFFILIATION_TYPE;

import com.example.rentalSystem.domain.member.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.member.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.member.student.dto.request.StudentUpdateRequest;
import com.example.rentalSystem.domain.member.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.member.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExample;
import com.example.rentalSystem.global.response.example.ApiErrorCodeExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "학생 관련 API")
public interface StudentControllerDocs {

    @Operation(summary = "회원가입 API")
    @ApiErrorCodeExamples({
        DUPLICATE_EMAIL_RESOURCE, INVALID_AFFILIATION_TYPE
    })
    ApiResponse<StudentSignUpResponse> signUp(StudentSignUpRequest signUpDto);

    @Operation(summary = "로그인 API")
    @ApiErrorCodeExamples({
        FAIL_AUTHENTICATION,
    })
    ApiResponse<JwtToken> signIn(StudentSignInRequest studentSignInRequest);

    @Operation(summary = "학생 전체 조회 API")
    ApiResponse<StudentListResponse> retrieveAllStudent();

    @Operation(summary = "학생 정보 수정 API")
    @ApiErrorCodeExample(ENTITY_NOT_FOUND)
    ApiResponse<?> updateStudentInfo(StudentUpdateRequest studentUpdateRequest,
        CustomerDetails customerDetails);
}
