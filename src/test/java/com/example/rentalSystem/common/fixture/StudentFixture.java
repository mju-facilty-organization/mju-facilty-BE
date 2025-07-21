package com.example.rentalSystem.common.fixture;

import static org.mockito.Mockito.doReturn;

import com.example.rentalSystem.domain.member.student.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.member.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.member.student.dto.response.StudentInfoResponse;
import com.example.rentalSystem.domain.member.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.member.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.member.student.entity.Student;
import java.util.List;
import org.mockito.Mockito;

public class StudentFixture {

    public static Student createStudent() {
        Student student = new Student(
            "60202489",
            "응용소프트웨어전공",
            0L,
            "testEmail@mju.ac.kr",
            "한상윤",
            "testPassword",
            "010-0000-0000");
        student = Mockito.spy(student);
        doReturn(1L).when(student).getId();
        return student;
    }

    public static StudentSignUpRequest studentSignUpRequest() {
        return new StudentSignUpRequest(
            "회원가입자",
            "60200000",
            "test1234!!",
            "testEmail@mju.ac.kr",
            "응용소프트웨어전공",
            "010-0000-0000"
        );
    }

    public static StudentSignUpResponse studentSignUpResponse() {
        return new StudentSignUpResponse("회원가입자");
    }

    public static StudentListResponse studentListResponse() {
        List<StudentInfoResponse> studentInfoResponseList = List.of(
            StudentInfoResponse.toStudentInfoResponse(createStudent()));
        return new StudentListResponse(studentInfoResponseList);
    }

    public static StudentSignInRequest studentSignInReqquest() {
        return new StudentSignInRequest(
            "gkstkddbs99@mju.ac.kr",
            "test1234!!"
        );
    }
}
