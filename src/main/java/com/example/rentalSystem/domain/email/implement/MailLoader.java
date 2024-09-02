package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.domain.email.entity.EMailVerification;
import com.example.rentalSystem.domain.email.repository.MailRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailLoader {

    private final MailRepository mailRepository;

    public String getAuthCode(String emailAddress) {
        EMailVerification emailVerification = mailRepository.findByEmailAddress(emailAddress)
            .orElseThrow((() -> new CustomException(ErrorType.EXPIRED_AUTH_TIME)));
        return emailVerification.getVerificationCode();
    }

    public EMailVerification getEmailVerification(String email) {
        return mailRepository.findByEmailAddress(email)
            .orElseThrow((() -> new CustomException(ErrorType.ENTITY_NOT_FOUND)));
    }
}
