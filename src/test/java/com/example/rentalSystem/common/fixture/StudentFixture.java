package com.example.rentalSystem.common.fixture;

import com.example.rentalSystem.domain.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import java.util.ArrayList;
import java.util.List;

public class StudentFixture {

    public static StudentSignUpRequest studentSignUpRequest() {
        return new StudentSignUpRequest(
            "회원가입자",
            "60200000",
            "test1234!!",
            "testEmail@mju.ac.kr",
            "응용소프트웨어전공",
            "융합소프트웨어학부",
            "010-0000-0000"
        );
    }

    public static StudentSignUpResponse studentSignUpResponse() {
        return new StudentSignUpResponse("회원가입자");
    }

    public static List<StudentResponse> studentListResponse() {
        List<StudentResponse> studentResponseList = new ArrayList<>();
        studentResponseList.add(new StudentResponse("회원가입자"));
        return studentResponseList;
    }

    public static StudentSignInRequest studentSignInReqquest() {
        return new StudentSignInRequest(
            "gkstkddbs99@mju.ac.kr",
            "test1234!!"
        );
    }
}
