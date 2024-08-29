package com.example.rentalSystem.domain.student.implement;

import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.global.auth.AuthUtil;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentManager {

    private final AuthUtil authUtil;
    private final PasswordEncoder passwordEncoder;
    private final StudentLoader studentLoader;
    private final StudentChecker studentChecker;
    private final StudentSaver studentSaver;


    @Transactional(rollbackFor = Exception.class)
    public StudentSignUpResponse studentSignUp(StudentSignUpRequest studentSignUpRequest) {
        studentChecker.checkExistUserEmail(studentSignUpRequest.email());
        String encodePassword = passwordEncoder.encode(studentSignUpRequest.password());
        Student student = studentSignUpRequest.toEntity(encodePassword);
        Student savedMember = studentSaver.save(student);
        return StudentSignUpResponse.form(savedMember.getName()
        );
    }

    @Transactional
    public JwtToken signIn(String loginId, String password) {
        return authUtil.createAuthenticationToken(loginId,password);
    }

    public StudentListResponse retrieveAllStudent() {

        List<StudentResponse> studentResponses = studentLoader.getStudentAll().stream()
            .map(this::toStudentResponse)
            .collect(Collectors.toList());

        return new StudentListResponse(studentResponses);
    }

    private StudentResponse toStudentResponse(Student student) {

        return new StudentResponse(student.getName());
    }
}
