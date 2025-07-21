package com.example.rentalSystem.domain.member.student.implement;

import com.example.rentalSystem.domain.member.student.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.member.student.repository.StudentRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentImpl {

    private final StudentRepository studentRepository;

    public List<StudentInfoResponse> retrieveAllStdent() {
        return getStudentAll().stream()
            .map(StudentInfoResponse::toSimpleStudentResponse)
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

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void checkExistUserEmail(String email) {
        if (studentRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL_RESOURCE);
        }
    }
}
