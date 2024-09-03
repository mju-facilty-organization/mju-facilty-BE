package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.domain.email.entity.Email;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailMaker {

    private static final String EMAIL_TITLE = "명지 Rental 이메일 인증 번호";

    public Email makeMail(String toEmail) {
        return Email
            .builder()
            .title(EMAIL_TITLE)
            .authCode(createCode())
            .emailAddress(toEmail)
            .build();
    }

    private String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(ErrorType.FAIL_CREATE_AUTH_CODE);
        }
    }

    public SimpleMailMessage createEmailForm(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.emailAddress());
        message.setSubject(email.title());
        message.setText(Email.INTRODUCE + email.authCode());
        return message;
    }
}
