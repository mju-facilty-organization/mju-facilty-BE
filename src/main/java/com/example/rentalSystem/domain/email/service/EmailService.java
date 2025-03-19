package com.example.rentalSystem.domain.email.service;

import com.example.rentalSystem.domain.email.controller.dto.request.EmailRequest;
import com.example.rentalSystem.domain.email.controller.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.email.implement.MailChecker;
import com.example.rentalSystem.domain.email.implement.MailHandler;
import com.example.rentalSystem.domain.email.implement.MailMaker;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final MailChecker mailChecker;
    private final MailMaker mailMaker;
    private final MailHandler mailHandler;
    private final JavaMailSender javaMailSender;

    public void checkDuplicatedEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateEmail(emailRequest.email());
    }

    public void sendCodeToEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateSend(emailRequest.email());
        String authCode = mailMaker.makeAuthCode();
        SimpleMailMessage emailForm = mailMaker.createEmailForm(emailRequest.email(), authCode);
        sendEmail(emailForm);
        mailHandler.save(emailRequest.email(), authCode);
    }

    private void sendEmail(SimpleMailMessage emailForm) {
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorType.FAIL_SEND_EMAIL);
        }
    }

    public EmailVerificationResult verificationCode(String email, String code) {
        String saveCode = mailHandler.getAuthCode(email);
        boolean verificationResult = mailChecker.checkAuthCode(saveCode, code);
        return EmailVerificationResult.from(verificationResult);
    }

    @Async("threadPoolTaskExecutor")
    public void sendProfessorRentalConfirm(String email) {
        String token = UUID.randomUUID().toString();
        mailHandler.saveToken(email, token);
        sendEmail(mailMaker.makeProfessorConfirmEmail(email, token));
    }
}
