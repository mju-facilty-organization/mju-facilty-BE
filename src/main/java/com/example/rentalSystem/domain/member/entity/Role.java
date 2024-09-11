package com.example.rentalSystem.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    STUDENT("ROLE_STUDENT"),
    PROFESSOR("ROLE_STUDENT,ROLE_PROFESSOR"),
    ADMIN("ROLE_STUDENT,ROLE_PROFESSOR,ROLE_ADMIN");
    
    private final String roles;

}
