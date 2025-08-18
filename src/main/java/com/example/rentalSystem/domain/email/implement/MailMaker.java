package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import jakarta.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class MailMaker {

    private final SpringTemplateEngine templateEngine;
    private final String AUTH_CODE_EMAIL_TITLE = "명지 Rental 이메일 인증 번호";
    private final String PROFESSOR_CONFIRM_EMAIL_TITLE = "명지대 시설 대여 승인에 관한 이메일입니다!";
    private final String CONFIRMATION_URL = "http://localhost:3000/approval/professor";
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

    public MimeMessage makeProfessorConfirmEmail(
        String email, Long approvalId,
        String token, MimeMessage mimeMessage
    ) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(PROFESSOR_CONFIRM_EMAIL_TITLE);

            // 템플릿에 전달할 데이터 설정
            Context context = new Context();
            String url = CONFIRMATION_URL + "/" + approvalId + "?token=" + token;
            context.setVariable("confirmationUrl", url);

            // 템플릿을 처리하여 HTML 문자열 생성
            String htmlContent = templateEngine.process("professor_email_template", context);
            helper.setText(htmlContent, true); // HTML 컨텐츠로 설정
            return mimeMessage;
        } catch (Exception e) {
            // 이메일 전송 실패 예외 처리
            throw new CustomException(ErrorType.FAIL_MAKE_EMAIL);
        }
    }


    public MimeMessage createEmailForm(String emailAddress, String authCode, MimeMessage mimeMessage) {
        try {

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(emailAddress);
            helper.setSubject(AUTH_CODE_EMAIL_TITLE);

            // 템플릿에 전달할 데이터 설정
            Context context = new Context();
            context.setVariable("title", AUTH_CODE_EMAIL_TITLE);
            context.setVariable("header", "이메일 인증 번호");
            context.setVariable("message", INTRODUCE);
            context.setVariable("authCode", authCode);

            // 템플릿을 처리하여 HTML 문자열 생성
            String htmlContent = templateEngine.process("email_template", context);
            helper.setText(htmlContent, true); // 두 번째 인자를 true로 설정하여 HTML 전송

            return mimeMessage;
        } catch (Exception e) {
            throw new CustomException(ErrorType.FAIL_MAKE_EMAIL);
        }
    }

}
