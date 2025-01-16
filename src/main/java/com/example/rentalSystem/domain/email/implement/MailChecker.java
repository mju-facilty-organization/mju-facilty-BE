package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.domain.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.email.entity.Email;
import com.example.rentalSystem.domain.member.implement.MemberLoader;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailChecker {

    private final MemberLoader memberLoader;

    public void checkDuplicateEmail(String email) {
        if (memberLoader.checkExistEmail(email)) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL_RESOURCE);
        }
    }

    public boolean checkAuthCode(String email, String code) {
        return email.equals(code);
    }
}

