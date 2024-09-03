package com.example.rentalSystem.domain.email.repository;

import com.example.rentalSystem.domain.email.entity.EMailVerification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<EMailVerification, Long> {


    Optional<EMailVerification> findByEmailAddress(String emailAddress);

    void deleteByEmailAddress(String emailAddress);

    void deleteByCertificationTrue();
}
