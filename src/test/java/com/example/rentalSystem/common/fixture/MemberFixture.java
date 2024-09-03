package com.example.rentalSystem.common.fixture;

import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import java.util.ArrayList;
import java.util.List;

public class MemberFixture {
    public static StudentSignUpRequest studentSignUpRequest(){
        return new StudentSignUpRequest(
            "회원가입자",
            "60200000",
            "test1234!!",
            "testEmail@mju.ac.kr",
            "ICT",
            "loginId",
            "응용소프트웨어전공",
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
}
