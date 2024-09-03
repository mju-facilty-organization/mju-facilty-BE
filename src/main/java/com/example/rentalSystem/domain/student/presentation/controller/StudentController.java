package com.example.rentalSystem.domain.student.presentation.controller;

import com.example.rentalSystem.domain.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.email.service.EmailFacade;
import com.example.rentalSystem.domain.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.presentation.api.StudentApi;
import com.example.rentalSystem.domain.student.service.StudentFacade;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StudentController implements StudentApi {

    private final StudentFacade studentFacade;
    private final EmailFacade emailFacade;

    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StudentSignUpResponse> signUp(
        StudentSignUpRequest signUpDto) {
        StudentSignUpResponse studentSignUpResponse = studentFacade.studentSignUp(signUpDto);
        return ApiResponse.success(SuccessType.CREATED, studentSignUpResponse);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<JwtToken> signIn(StudentSignInRequest studentSignInRequest) {
        String loginId = studentSignInRequest.loginId();
        String password = studentSignInRequest.password();
        JwtToken jwtToken = studentFacade.userSignIN(loginId, password);
        return ApiResponse.success(SuccessType.SUCCESS, jwtToken);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<StudentListResponse> retrieveAllStudent() {
        return ApiResponse.success(SuccessType.SUCCESS, studentFacade.retrieveAllStudent());
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> checkDuplicatedEmail(EmailRequest emailRequest) {
        emailFacade.checkDuplicatedEmail(emailRequest.email());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> sendCodeToEmail(EmailRequest emailRequest) {
        emailFacade.sendCodeToEmail(emailRequest.email());
        return ApiResponse.success(SuccessType.SUCCESS);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> checkCode(EmailRequest emailRequest) {
        EmailVerificationResult emailVerificationResult = emailFacade.verificationCode(
            emailRequest.email(), emailRequest.code());
        return ApiResponse.success(SuccessType.SUCCESS, emailVerificationResult);
    }

}
