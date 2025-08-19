package com.example.rentalSystem.domain.notice.repository;

import com.example.rentalSystem.domain.notice.domain.entity.Notice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Optional<Notice> findFirstByOrderByCreatedAtDesc();

}
