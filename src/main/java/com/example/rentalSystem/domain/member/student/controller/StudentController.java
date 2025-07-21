package com.example.rentalSystem.domain.member.student.controller;

import com.example.rentalSystem.domain.member.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.member.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.member.student.dto.request.StudentUpdateRequest;
import com.example.rentalSystem.domain.member.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.member.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.member.student.dto.response.StudentUpdateResponse;
import com.example.rentalSystem.domain.member.student.service.StudentService;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.type.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController implements StudentControllerDocs {

    private final StudentService studentService;

    @Override
    @PostMapping("/sign-up")
    public ApiResponse<StudentSignUpResponse> signUp(
        @RequestBody StudentSignUpRequest signUpDto) {
        StudentSignUpResponse studentSignUpResponse = studentService.studentSignUp(signUpDto);
        return ApiResponse.success(SuccessType.CREATED, studentSignUpResponse);
    }

    @Override
    @PostMapping("/sign-in")
    public ApiResponse<JwtToken> signIn(
        @RequestBody StudentSignInRequest studentSignInRequest) {
        JwtToken jwtToken = studentService.userSignIn(studentSignInRequest);
        return ApiResponse.success(SuccessType.SUCCESS, jwtToken);
    }

    @Override
    @GetMapping
    public ApiResponse<StudentListResponse> retrieveAllStudent() {
        return ApiResponse.success(SuccessType.SUCCESS, studentService.retrieveAllStudent());
    }

    @Override
    @PatchMapping
    public ApiResponse<?> updateStudentInfo(StudentUpdateRequest studentUpdateRequest,
        CustomerDetails customerDetails) {
        StudentUpdateResponse studentUpdateResponse = studentService.updateStudentInfo(
            studentUpdateRequest, customerDetails.getUsername());
        return ApiResponse.success(SuccessType.SUCCESS, studentUpdateResponse);
    }
}
