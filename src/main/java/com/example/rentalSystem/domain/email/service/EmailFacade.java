package com.example.rentalSystem.domain.email.service;

import com.example.rentalSystem.domain.email.dto.response.EmailVerificationResult;
import com.example.rentalSystem.domain.email.entity.EMailVerification;
import com.example.rentalSystem.domain.email.entity.Email;
import com.example.rentalSystem.domain.email.implement.MailChecker;
import com.example.rentalSystem.domain.email.implement.MailDeleter;
import com.example.rentalSystem.domain.email.implement.MailLoader;
import com.example.rentalSystem.domain.email.implement.MailMaker;
import com.example.rentalSystem.domain.email.implement.MailSaver;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailFacade {

    private final long authCodeExpirationMillis = 1800000;

    private final MailChecker mailChecker;
    private final MailMaker mailMaker;
    private final MailSaver mailSaver;
    private final MailLoader mailLoader;
    private final JavaMailSender javaMailSender;
    private final MailDeleter mailDeleter;

    public void checkDuplicatedEmail(String email) {
        mailChecker.checkDuplicateEmail(email);
    }

    @Transactional
    public void sendCodeToEmail(String emailAddress) {
        mailDeleter.deleteByEmailAddress(emailAddress);
        Email email = mailMaker.makeMail(emailAddress);
        SimpleMailMessage emailForm = mailMaker.createEmailForm(email);
        sendEmail(emailForm);
        mailSaver.saveEmailAuthCode(emailAddress, email.authCode());
    }

    @Scheduled(fixedRate = authCodeExpirationMillis)
    public void deleteExpiredMail() {
        mailDeleter.deleteCertifyEmail();
    }

    @Transactional
    public EmailVerificationResult verificationCode(String email, String code) {
        String saveCode = mailLoader.getAuthCode(email);
        EmailVerificationResult emailVerificationResult = mailChecker.checkAuthCode(saveCode, code);
        EMailVerification eMailVerification = mailLoader.getEmailVerification(email);
        eMailVerification.toCertify();
        return emailVerificationResult;
    }

    private void sendEmail(SimpleMailMessage emailForm) {
        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new CustomException(ErrorType.FAIL_SEND_EMAIL);
        }
    }
}
