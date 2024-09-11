package com.example.rentalSystem.domain.student.presentation.api;

import static com.example.rentalSystem.global.config.SecurityConfig.SERVER_PREFIX;

import com.example.rentalSystem.domain.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentUpdateRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import com.example.rentalSystem.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(SERVER_PREFIX + "/students")
@Tag(name = "학생", description = "회원가입,로그인 API")
public interface StudentApi {

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/sign-up")
    ApiResponse<StudentSignUpResponse> signUp(
        @RequestBody StudentSignUpRequest studentSignUpRequest);

    @Operation(summary = "로그인")
    @GetMapping("/sign-in")
    ApiResponse<JwtToken> signIn(
        @RequestBody StudentSignInRequest studentSignInRequest);

    @Operation(summary = "학생 전체 조회")
    @GetMapping
    ApiResponse<StudentListResponse> retrieveAllStudent();

    @GetMapping("/sign-up/emails/check-email")
    @Operation(summary = "이메일 중복확인")
    ApiResponse<?> checkDuplicatedEmail(@RequestBody EmailRequest emailRequest);

    @PostMapping("/sign-up/emails/request-code")
    @Operation(summary = "이메일 인증 코드 요청")
    ApiResponse<?> sendCodeToEmail(@RequestBody EmailRequest emailRequest);

    @GetMapping("/sign-up/emails/check-code")
    @Operation(summary = "이메일 인증 코드 검증")
    ApiResponse<?> checkCode(@RequestBody EmailRequest emailRequest);

    @PatchMapping
    @Operation(summary = "학생 정보 변경")
    ApiResponse<?> updateStudentInfo(@RequestBody StudentUpdateRequest studentUpdateRequest,
        @AuthenticationPrincipal CustomerDetails customerDetails);
}
