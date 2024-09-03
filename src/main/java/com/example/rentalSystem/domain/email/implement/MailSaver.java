package com.example.rentalSystem.domain.email.implement;


import com.example.rentalSystem.domain.email.entity.EMailVerification;
import com.example.rentalSystem.domain.email.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSaver {

    private final MailRepository mailRepository;

    public void saveEmailAuthCode(String emailAddress, String authCode) {
        mailRepository.save(
            EMailVerification.builder().emailAddress(emailAddress).verificationCode(authCode)
                .build());
    }
}
