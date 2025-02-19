package com.example.rentalSystem.domain.email.repository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class MailRepository {

    private final Map<String, String> authMailRepository = new HashMap<>();
    private final Map<String, String> professorTokenRepository = new HashMap<>();

    public void deleteByEmailAddress(String emailAddress) {
        authMailRepository.remove(emailAddress);
    }

    public void save(String emailAddress, String authCode) {
        authMailRepository.put(emailAddress, authCode);
    }

    public void deleteExpiredEmail() {
        authMailRepository.clear();
    }

    public String findByEmailAddress(String emailAddress) {
        return authMailRepository.get(emailAddress);
    }

    public boolean checkExistEmail(String email) {
        return authMailRepository.containsKey(email);
    }

    public void saveToken(String professorEmail, String token) {
        authMailRepository.put(professorEmail, token);
    }
}
