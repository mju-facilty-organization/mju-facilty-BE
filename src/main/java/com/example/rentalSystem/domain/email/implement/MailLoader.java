package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.domain.email.repository.MailRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailLoader {

    private final MailRepository mailRepository;

    public String getAuthCode(String emailAddress) {
        String authCode = mailRepository.findByEmailAddress(emailAddress);
        if (Objects.isNull(authCode)) {
            throw new CustomException(ErrorType.ENTITY_NOT_FOUND);
        }
        return authCode;
    }
}
