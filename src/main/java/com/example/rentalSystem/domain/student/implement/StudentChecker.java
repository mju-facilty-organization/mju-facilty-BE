package com.example.rentalSystem.domain.student.implement;

import com.example.rentalSystem.domain.student.repository.StudentRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentChecker {

    private final StudentRepository studentRepository;

    public void checkExistUserEmail(String email) {
        if (studentRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorType.ENTITY_NOT_FOUND);
        }
    }

}
