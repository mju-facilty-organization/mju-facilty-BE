package com.example.rentalSystem.domain.student.presentation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.rentalSystem.common.fixture.MemberFixture;
import com.example.rentalSystem.domain.student.dto.request.StudentSignUpRequest;
import com.example.rentalSystem.domain.student.dto.response.StudentListResponse;
import com.example.rentalSystem.domain.student.dto.response.StudentSignUpResponse;
import com.example.rentalSystem.domain.student.presentation.controller.StudentController;
import com.example.rentalSystem.domain.student.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@Slf4j
@WebMvcTest(controllers = StudentController.class)
@ExtendWith(SpringExtension.class)
public class StudentControllerTest {

    @MockBean
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("회원 가입 성공")
    public void 회원가입_학생_성공() throws Exception {
        //given
        StudentSignUpRequest studentSignUpRequest = MemberFixture.studentSignUpRequest();
        StudentSignUpResponse studentSignUpResponse = MemberFixture.studentSignUpResponse();
        doReturn(studentSignUpResponse).when(studentService)
            .studentSignUp(any(StudentSignUpRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/students/sign-up")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(studentSignUpRequest)));

        // then
        resultActions.andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.studentName", studentSignUpResponse).exists());
    }

    @DisplayName("전체 학생 목록 조회")
    @Test
    void retrieveAllStudent() throws Exception {
        //given
        doReturn(MemberFixture.studentListResponse()).when(studentService).retrieveAllStudent();

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/student")
        );
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        StudentListResponse response = formJson(mvcResult.getResponse().getContentAsString(),
            StudentListResponse.class);

        assertThat(response.studentResponseList().size()).isEqualTo(1);
    }

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    protected <T> T formJson(String string, Class<T> tClass) throws JsonProcessingException {
        return objectMapper.readValue(string, tClass);
    }
}
