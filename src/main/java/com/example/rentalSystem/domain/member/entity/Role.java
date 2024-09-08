package com.example.rentalSystem.domain.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    STUDENT("ROLE_STUDENT"),
    PROFESSOR("ROLE_STUDENT,ROLE_PROFESSOR"),
    ADMIN("ROLE_STUDENT,ROLE_PROFESSOR,ROLE_ADMIN");
    private final String roles;

    Role(String roles) {
        this.roles = roles;
    }

}
