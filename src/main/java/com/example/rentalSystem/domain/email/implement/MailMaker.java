package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
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
    public static final String INTRODUCE = "명지대 Rental 가입 인증 코드입니다.\n";

    public String makeAuthCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
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


    public SimpleMailMessage createEmailForm(String emailAddress, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailAddress);
        message.setSubject(AUTH_CODE_EMAIL_TITLE);
        message.setText(INTRODUCE + authCode);
        return message;
    }

}
