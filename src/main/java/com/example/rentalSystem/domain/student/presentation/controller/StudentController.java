package com.example.rentalSystem.domain.student.presentation.controller;

import com.example.rentalSystem.domain.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.presentation.api.StudentApi;
import com.example.rentalSystem.domain.student.service.StudentService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RequiredArgsConstructor
@RestController
public class StudentController implements StudentApi {

    private final StudentService studentService;

    public ResponseEntity<ApiResponse<StudentSignUpResponse>> signUp(StudentSignUpRequest signUpDto) {
        StudentSignUpResponse studentSignUpResponse = studentService.studentSignUp(signUpDto);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.CREATED,studentSignUpResponse));
    }

    public ResponseEntity<ApiResponse<JwtToken>> signIn(StudentSignInRequest studentSignInRequest) {
        String loginId = studentSignInRequest.loginId();
        String password = studentSignInRequest.password();
        JwtToken jwtToken = studentService.userSignIN(loginId, password);
        return ResponseEntity.ok(ApiResponse.success(SuccessType.SUCCESS, jwtToken));
    }

    @Override
    public ResponseEntity<ApiResponse<StudentListResponse>> retrieveAllStudent() {
        return ResponseEntity.ok(ApiResponse.success(SuccessType.SUCCESS,studentService.retrieveAllStudent()));
    }

}
