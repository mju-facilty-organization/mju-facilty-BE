package com.example.rentalSystem.domain.email.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MailRepository {

    private final Map<String, String> mailRepository = new HashMap<>();


    public void deleteByEmailAddress(String emailAddress) {
        mailRepository.remove(emailAddress);
    }

    public void save(String emailAddress, String authCode) {
        mailRepository.put(emailAddress, authCode);
    }

    public void deleteExpiredEmail() {
        mailRepository.clear();
    }

    public String findByEmailAddress(String emailAddress) {
        return mailRepository.get(emailAddress);
    }
}
