package com.example.rentalSystem.domain.email.implement;

import com.example.rentalSystem.domain.email.repository.MailRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailDeleter {

    private final MailRepository mailRepository;

    public void deleteByEmailAddress(String emailAddress) {
        mailRepository.findByEmailAddress(emailAddress)
            .ifPresent(mail -> mailRepository.deleteByEmailAddress(emailAddress));
    }

    public void deleteExpiredMail(String emailAddress) {
        mailRepository.deleteByEmailAddress(emailAddress);
    }

    public void deleteCertifyEmail() {
        mailRepository.deleteByCertificationTrue();
    }
}
