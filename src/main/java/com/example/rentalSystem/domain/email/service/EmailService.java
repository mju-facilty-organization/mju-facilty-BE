package com.example.rentalSystem.domain.email.service;

import com.example.rentalSystem.domain.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.email.implement.MailChecker;
import com.example.rentalSystem.domain.email.implement.MailImpl;
import com.example.rentalSystem.domain.email.implement.MailMaker;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final MailChecker mailChecker;
    private final MailMaker mailMaker;
    private final MailImpl mailImpl;
    private final JavaMailSender javaMailSender;

    public void checkDuplicatedEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateEmail(emailRequest.email());
    }

    public void sendCodeToEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateSend(emailRequest.email());
        String authCode = mailMaker.makeAuthCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessage message = mailMaker.createEmailForm(emailRequest.email(), authCode, mimeMessage);
        sendEmail(message);
        mailImpl.save(emailRequest.email(), authCode);
    }

    private void sendEmail(MimeMessage emailForm) {
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorType.FAIL_SEND_EMAIL);
        }
    }

    public EmailVerificationResult verificationCode(String email, String code) {
        String saveCode = mailImpl.getAuthCode(email);
        boolean verificationResult = mailChecker.checkAuthCode(saveCode, code);
        return EmailVerificationResult.from(verificationResult);
    }

    @Async("threadPoolTaskExecutor")
    public void sendProfessorRentalConfirm(String email, Long approvalId) {
        String token = UUID.randomUUID().toString();
        mailImpl.saveToken(token, email);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessage message = mailMaker.makeProfessorConfirmEmail(email, approvalId, token, mimeMessage);
        sendEmail(message);
    }
}
