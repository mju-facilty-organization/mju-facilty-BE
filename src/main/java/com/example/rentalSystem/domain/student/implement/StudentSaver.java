package com.example.rentalSystem.domain.student.implement;


import com.example.rentalSystem.domain.student.entity.Student;
import com.example.rentalSystem.domain.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentSaver {
    private final StudentRepository studentRepository;

    public Student save(Student student) {
        return studentRepository.save(student);
    }

}
