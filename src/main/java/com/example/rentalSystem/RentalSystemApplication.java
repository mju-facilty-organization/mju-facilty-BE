package com.example.rentalSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {
    "com.example.rentalSystem"
})
public class RentalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalSystemApplication.class, args);
        // test
    }

}
