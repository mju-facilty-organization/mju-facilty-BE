package com.example.rentalSystem.domain.student.controller;

import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentUpdateRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentUpdateResponse;
import com.example.rentalSystem.domain.student.service.StudentService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StudentSignUpResponse> signUp(
        StudentSignUpRequest signUpDto) {
        StudentSignUpResponse studentSignUpResponse = studentService.studentSignUp(signUpDto);
        return ApiResponse.success(SuccessType.CREATED, studentSignUpResponse);
    }

    @GetMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<JwtToken> signIn(StudentSignInRequest studentSignInRequest) {
        JwtToken jwtToken = studentService.userSignIn(studentSignInRequest);
        return ApiResponse.success(SuccessType.SUCCESS, jwtToken);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<StudentListResponse> retrieveAllStudent() {
        return ApiResponse.success(SuccessType.SUCCESS, studentService.retrieveAllStudent());
    }

    @PatchMapping
    public ApiResponse<?> updateStudentInfo(StudentUpdateRequest studentUpdateRequest,
        CustomerDetails customerDetails) {
        StudentUpdateResponse studentUpdateResponse = studentService.updateStudentInfo(
            studentUpdateRequest, customerDetails.getUsername());
        return ApiResponse.success(SuccessType.SUCCESS, studentUpdateResponse);
    }
}
