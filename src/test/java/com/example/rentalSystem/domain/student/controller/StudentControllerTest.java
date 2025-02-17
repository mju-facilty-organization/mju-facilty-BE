package com.example.rentalSystem.domain.student.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.rentalSystem.common.fixture.StudentFixture;
import com.example.rentalSystem.common.support.ApiTestSupport;
import com.example.rentalSystem.domain.student.controller.dto.request.StudentSignInRequest;
import com.example.rentalSystem.domain.student.controller.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.controller.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.service.StudentService;
import com.example.rentalSystem.global.auth.jwt.entity.JwtToken;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import com.example.rentalSystem.global.response.ResultType;
import com.example.rentalSystem.global.response.SuccessType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StudentController.class)
public class StudentControllerTest extends ApiTestSupport {

    @MockBean
    private StudentService studentService;


    @Test
    @WithMockUser
    public void 회원가입_학생_성공() throws Exception {
        //given
        StudentSignUpRequest studentSignUpRequest = StudentFixture.studentSignUpRequest();
        StudentSignUpResponse studentSignUpResponse = StudentFixture.studentSignUpResponse();
        doReturn(studentSignUpResponse).when(studentService)
            .studentSignUp(any(StudentSignUpRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/students/sign-up")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(studentSignUpRequest)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("students/회원가입 성공",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // then
        resultActions
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.CREATED.getHttpStatusCode()))
            .andExpect(jsonPath("$.data.studentName", studentSignUpResponse).exists());
    }

    @Test
    @WithMockUser
    public void 회원가입_학생_실패() throws Exception {
        // given
        StudentSignUpRequest studentSignUpRequest = StudentFixture.studentSignUpRequest();
        doThrow(new CustomException(ErrorType.DUPLICATE_EMAIL_RESOURCE)).when(studentService)
            .studentSignUp(any(StudentSignUpRequest.class));

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/students/sign-up")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(studentSignUpRequest)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("students/회원가입 실패",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // then
        resultActions.andExpect(status().isConflict()) // 409 Conflict
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.FAIL)))
            .andExpect(
                jsonPath("$.httpStatusCode").value(
                    ErrorType.DUPLICATE_EMAIL_RESOURCE.getHttpStatusCode())) // 에러 코드 확인
            .andExpect(jsonPath("$.message").value(
                ErrorType.DUPLICATE_EMAIL_RESOURCE.getMessage())); // 에러 메시지 확인

    }

    @Test
    @WithMockUser
    void 로그인_학생_성공() throws Exception {
        // given
        StudentSignInRequest studentSignInRequest = StudentFixture.studentSignInReqquest();
        JwtToken jwtToken = JwtToken.builder()
            .grantType("ROLE_STUDENT")
            .accessToken("accessToken")
            .refreshToken("refresh token")
            .build();
        doReturn(jwtToken).when(studentService).userSignIn(any(StudentSignInRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/students/sign-in")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(studentSignInRequest)))
            .andDo(MockMvcRestDocumentation.document("students/로그인성공",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.SUCCESS.getHttpStatusCode()));
    }

    @Test
    @WithMockUser
    void retrieveAllStudent() throws Exception {
        //given
        doReturn(StudentFixture.studentListResponse())
            .when(studentService)
            .retrieveAllStudent();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/students")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcRestDocumentation.document("students/학생 전체 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        resultActions.andExpect(status().isOk());
    }


}

