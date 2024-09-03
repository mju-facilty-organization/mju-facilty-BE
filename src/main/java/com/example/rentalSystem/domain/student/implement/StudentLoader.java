package com.example.rentalSystem.domain.student.implement;

import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.domain.student.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StudentLoader {
        private final StudentRepository studentRepository;

    public List<Student> getStudentAll() {
        return studentRepository.findAll();
    }
}
