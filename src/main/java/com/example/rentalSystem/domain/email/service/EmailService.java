package com.example.rentalSystem.domain.email.service;

import com.example.rentalSystem.domain.email.controller.dto.request.EmailRequest;
import com.example.rentalSystem.domain.email.controller.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.email.entity.AuthCodeEmail;
import com.example.rentalSystem.domain.email.implement.MailChecker;
import com.example.rentalSystem.domain.email.implement.MailLoader;
import com.example.rentalSystem.domain.email.implement.MailMaker;
import com.example.rentalSystem.domain.email.repository.MailRepository;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final long authCodeExpirationMillis = 1800000;

    private final MailChecker mailChecker;
    private final MailMaker mailMaker;
    private final MailLoader mailLoader;
    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;

    public void checkDuplicatedEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateEmail(emailRequest.email());
    }

    @Transactional
    public void sendCodeToEmail(EmailRequest emailRequest) {
        mailChecker.checkDuplicateSend(emailRequest.email());
        AuthCodeEmail authCodeEmail = mailMaker.makeAuthCodeMail(emailRequest.email());
        SimpleMailMessage emailForm = mailMaker.createEmailForm(authCodeEmail);
        sendEmail(emailForm);
        mailRepository.save(emailRequest.email(), authCodeEmail.authCode());
    }

    private void sendEmail(SimpleMailMessage emailForm) {
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorType.FAIL_SEND_EMAIL);
        }
    }

    @Scheduled(fixedRate = authCodeExpirationMillis)
    public void deleteExpiredMail() {
        mailRepository.deleteExpiredEmail();
    }

    @Transactional
    public EmailVerificationResult verificationCode(String email, String code) {
        String saveCode = mailLoader.getAuthCode(email);
        boolean verificationResult = mailChecker.checkAuthCode(saveCode, code);
        return EmailVerificationResult.of(verificationResult);
    }


    public void sendProfessorRentalConfirm(String email) {
        String token = UUID.randomUUID().toString();
        mailRepository.saveToken(email, token);
        sendEmail(mailMaker.makeProfessorConfirmEmail(email, token));
    }
}
