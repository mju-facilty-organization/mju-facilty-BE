package com.example.rentalSystem.domain.member.entity;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "member_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // or GenerationType.AUTO
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    protected String name;
    private String password;
    private String phoneNumber;
    private AffiliationType college;

    public Member(String email, Role role, String name,
        String password, String phoneNumber, String major) {
        this.email = email;
        this.role = role;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.college = AffiliationType.getCollegeByMajor(major);
    }

    public String getRoles() {
        return role.getRoles();
    }
}
