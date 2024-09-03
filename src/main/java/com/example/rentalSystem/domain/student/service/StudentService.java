package com.example.rentalSystem.domain.student.service;

import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.implement.StudentManager;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import com.example.rentalSystem.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentManager studentManager;

    public StudentSignUpResponse studentSignUp(StudentSignUpRequest signUpDto) {
        return studentManager.studentSignUp(signUpDto);
    }

    public JwtToken userSignIN(String loginId, String password) {
        return studentManager.signIn(loginId,password);
    }

    public StudentListResponse retrieveAllStudent() {
        return studentManager.retrieveAllStudent();
    }
}
