package com.example.rentalSystem.domain.student.implement;

import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.domain.student.repository.StudentRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentFinder {

    private final StudentRepository studentRepository;

    public List<StudentResponse> retrieveAllStdent() {
        return getStudentAll().stream()
            .map(StudentResponse::toStudentResponse)
            .collect(Collectors.toList());
    }

    private List<Student> getStudentAll() {
        return studentRepository.findAll();
    }

    public Student findByEmail(String email) {
        return studentRepository.findByEmail(email).orElseThrow(() -> new CustomException(
            ErrorType.ENTITY_NOT_FOUND));
    }

    public Student findById(String studentId) {
        return studentRepository.findById(Long.valueOf(studentId))
            .orElseThrow(() -> new CustomException(
                ErrorType.ENTITY_NOT_FOUND));
    }
}
