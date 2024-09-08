package com.example.rentalSystem.domain.student.service;

import com.example.rentalSystem.domain.member.entity.Member;
import com.example.rentalSystem.domain.member.implement.MemberLoader;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentUpdateRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentUpdateResponse;
import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.domain.student.implement.StudentChecker;
import com.example.rentalSystem.domain.student.implement.StudentFinder;
import com.example.rentalSystem.domain.student.implement.StudentSaver;
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
public class StudentFacade {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final MemberLoader memberLoader;
    private final StudentFinder studentFinder;
    private final StudentChecker studentChecker;
    private final StudentSaver studentSaver;

    @Transactional(rollbackFor = Exception.class)
    public StudentSignUpResponse studentSignUp(StudentSignUpRequest studentSignUpRequest) {
        studentChecker.checkExistUserEmail(studentSignUpRequest.email());
        String encodePassword = passwordEncoder.encode(studentSignUpRequest.password());
        Student student = studentSignUpRequest.toEntity(encodePassword);
        Student savedMember = studentSaver.save(student);
        return StudentSignUpResponse.from(savedMember.getName());
    }

    @Transactional
    public JwtToken userSignIn(String loginId, String password) {
        return authService.createAuthenticationToken(loginId, password);
    }

    public StudentListResponse retrieveAllStudent() {
        List<StudentResponse> studentResponses = studentFinder.retrieveAllStdent();
        return new StudentListResponse(studentResponses);
    }

    @Transactional
    public StudentUpdateResponse updateStudentInfo(StudentUpdateRequest studentUpdateRequest,
        String memberLoginId) {
        Student student = (Student) memberLoader.findByLoginId(memberLoginId);
        student.updateInfo(studentUpdateRequest);
        return StudentUpdateResponse.from(student.getName(), student.getMajor());
    }

}
