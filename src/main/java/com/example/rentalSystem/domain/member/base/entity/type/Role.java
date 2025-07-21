package com.example.rentalSystem.domain.member.base.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    STUDENT("ROLE_STUDENT"),
    PROFESSOR("ROLE_STUDENT,ROLE_PROFESSOR"),
    PIC("ROLE_STUDENT,ROLE_PROFESSOR,ROLE_PIC"),
    ADMIN("ROLE_STUDENT,ROLE_PROFESSOR,ROLE_PIC,ROLE_ADMIN");

    private final String roles;

}
