package com.example.rentalSystem.domain.student.implement;

import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.domain.student.repository.StudentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentLoader {

    private final StudentRepository studentRepository;
    
    public List<StudentResponse> retrieveAllStdent() {
        return getStudentAll().stream()
            .map(this::toStudentResponse)
            .collect(Collectors.toList());
    }

    private List<Student> getStudentAll() {
        return studentRepository.findAll();
    }

    private StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(student.getName());
    }
}
