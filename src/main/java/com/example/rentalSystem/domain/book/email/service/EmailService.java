package com.example.rentalSystem.domain.book.email.service;

import com.example.rentalSystem.domain.book.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.book.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.book.email.implement.MailChecker;
import com.example.rentalSystem.domain.book.email.implement.MailImpl;
import com.example.rentalSystem.domain.book.email.implement.MailMaker;
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
    private final MailImpl mailImpl;
    private final JavaMailSender javaMailSender;

    public void checkDuplicatedEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateEmail(emailRequest.email());
    }

    public void sendCodeToEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateSend(emailRequest.email());
        String authCode = mailMaker.makeAuthCode();
        SimpleMailMessage emailForm = mailMaker.createEmailForm(emailRequest.email(), authCode);
        sendEmail(emailForm);
        mailImpl.save(emailRequest.email(), authCode);
    }

    private void sendEmail(SimpleMailMessage emailForm) {
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
        sendEmail(mailMaker.makeProfessorConfirmEmail(email, approvalId, token));
    }
}
