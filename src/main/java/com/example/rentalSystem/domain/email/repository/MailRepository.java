package com.example.rentalSystem.domain.email.repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Repository;

@Repository
public class MailRepository {

    private final long authCodeExpirationMINUTES = 5;

    private final Cache<String, String> authMailRepository = CacheBuilder.newBuilder()
        .expireAfterWrite(authCodeExpirationMINUTES, TimeUnit.MINUTES)
        .maximumSize(100)
        .build();
    private final Map<String, String> professorTokenRepository = new HashMap<>();

    public void save(String emailAddress, String authCode) {
        authMailRepository.put(emailAddress, authCode);
    }

    public String findByEmailAddress(String emailAddress) {
        return authMailRepository.getIfPresent(emailAddress);
    }

    public boolean isEmailDuplicated(String email) {
        return findByEmailAddress(email) != null;
    }

    public void saveToken(String token, String professorEmail) {
        professorTokenRepository.put(token, professorEmail);
    }

    public String getProfessorEmail(String token) {
        return professorTokenRepository.get(token);
    }
}
