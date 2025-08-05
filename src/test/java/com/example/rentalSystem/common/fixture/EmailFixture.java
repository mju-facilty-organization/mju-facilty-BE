package com.example.rentalSystem.common.fixture;

import com.example.rentalSystem.domain.book.email.dto.request.EmailRequest;

public class EmailFixture {

    public static EmailRequest emailRequest() {
        return new EmailRequest(
            "gkstkddbs99@mju.ac.kr"
        );
    }
}
