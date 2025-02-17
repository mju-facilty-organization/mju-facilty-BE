package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.domain.email.entity.AuthCodeEmail;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailMaker {

    private final String AUTH_CODE_EMAIL_TITLE = "명지 Rental 이메일 인증 번호";
    private final String PROFESSOR_CONFIRM_EMAIL_TITLE = "명지대 대한 시설 대여 승인 이메일입니다.";
    private final String CONFIRMATION_URL = "https://yourapp.com/request/confirm?token=";
    private final String PROFESSOR_CONFIRM_EMAIL_CONTENT = "명지대 시설 대여에 대한 승인 요청입니다.\n아래 링크를 클릭하여 요청을 확인해주세요:\n";

    public AuthCodeEmail makeAuthCodeMail(String toEmail) {
        return AuthCodeEmail
            .builder()
            .title(AUTH_CODE_EMAIL_TITLE)
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

    public SimpleMailMessage makeProfessorConfirmEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(PROFESSOR_CONFIRM_EMAIL_TITLE);
        String url = CONFIRMATION_URL + token;
        message.setText(PROFESSOR_CONFIRM_EMAIL_CONTENT + url);
        return message;
    }


    public SimpleMailMessage createEmailForm(AuthCodeEmail authCodeEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(authCodeEmail.emailAddress());
        message.setSubject(authCodeEmail.title());
        message.setText(AuthCodeEmail.INTRODUCE + authCodeEmail.authCode());
        return message;
    }

}
