package com.example.rentalSystem.domain.member.base.repository;

import com.example.rentalSystem.domain.member.base.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String memberEmail);
}
