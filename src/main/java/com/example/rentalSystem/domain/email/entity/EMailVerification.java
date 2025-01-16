package com.example.rentalSystem.domain.email.entity;

import lombok.Data;

@Data
public class EMailVerification {

    private String emailAddress;
    private String verificationCode;

}
