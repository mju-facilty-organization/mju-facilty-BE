package com.example.rentalSystem.domain.rental.email.implement;

import com.example.rentalSystem.domain.rental.email.repository.MailRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailChecker {

    private final MailRepository mailRepository;

    public void checkDuplicateEmail(String email) {
        if (mailRepository.isEmailDuplicated(email)) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL_RESOURCE);
        }
    }

    public boolean checkAuthCode(String saveCode, String code) {
        return saveCode.equals(code);
    }

    public void checkDuplicateSend(String email) {
        if (mailRepository.isEmailDuplicated(email)) {
            throw new CustomException(ErrorType.DUPLICATE_SEND);
        }
    }
}

