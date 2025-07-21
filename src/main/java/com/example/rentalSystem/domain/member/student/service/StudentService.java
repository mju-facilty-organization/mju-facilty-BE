package com.example.rentalSystem.domain.member.student.service;

import com.example.rentalSystem.domain.member.student.controller.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.member.student.controller.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.member.student.controller.dto.request.StudentUpdateRequest;
import com.example.rentalSystem.domain.member.student.controller.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.member.student.controller.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.member.student.controller.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.member.student.controller.dto.response.StudentUpdateResponse;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.member.student.implement.StudentImpl;
import com.example.rentalSystem.global.auth.AuthService;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class StudentService {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final StudentImpl studentImpl;

    @Transactional(rollbackFor = Exception.class)
    public StudentSignUpResponse studentSignUp(StudentSignUpRequest studentSignUpRequest) {
        studentImpl.checkExistUserEmail(studentSignUpRequest.email());
        String encodePassword = passwordEncoder.encode(studentSignUpRequest.password());
        Student student = studentSignUpRequest.toEntity(encodePassword);
        Student savedMember = studentImpl.save(student);
        return StudentSignUpResponse.from(savedMember.getName());
    }

    @Transactional
    public JwtToken userSignIn(StudentSignInRequest studentSignInRequest) {
        return authService.createAuthenticationToken(studentSignInRequest.email(),
            studentSignInRequest.password());
    }

    public StudentListResponse retrieveAllStudent() {
        List<StudentInfoResponse> studentInfoResponses = studentImpl.retrieveAllStdent();
        return new StudentListResponse(studentInfoResponses);
    }

    @Transactional
    public StudentUpdateResponse updateStudentInfo(StudentUpdateRequest studentUpdateRequest,
        String email) {
        Student student = studentImpl.findByEmail(email);
        student.updateInfo(studentUpdateRequest.name(), studentUpdateRequest.major());
        return StudentUpdateResponse.of(student.getName(), student.getMajorName());
    }
}
