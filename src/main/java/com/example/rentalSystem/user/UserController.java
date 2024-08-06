package com.example.rentalSystem.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController{
    @GetMapping("hello")
    public String getHello(){
    return "hello";
    }
    @GetMapping("/")
    public String hello() {
        return "eroom 배포 자동화 테스트";
    }

    @GetMapping("hi")
    public String hi() {
    return "hi";
    }
}
