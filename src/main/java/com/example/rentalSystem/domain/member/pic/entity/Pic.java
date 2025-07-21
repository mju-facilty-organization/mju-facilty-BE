package com.example.rentalSystem.domain.member.pic.entity;

import com.example.rentalSystem.domain.member.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.member.base.entity.Member;
import com.example.rentalSystem.domain.member.base.entity.type.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@AllArgsConstructor
public class Pic extends Member {

    @Builder
    public Pic(
        String email,
        Role role,
        String name,
        String password,
        String phoneNumber,
        String college) {
        super(email, role, name, password, phoneNumber, AffiliationType.getInstance(college));
    }
}
