package com.example.rentalSystem.domain.email;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.rentalSystem.common.fixture.EmailFixture;
import com.example.rentalSystem.common.support.ApiTestSupport;
import com.example.rentalSystem.domain.email.controller.EmailController;
import com.example.rentalSystem.domain.email.dto.request.EmailRequest;
import com.example.rentalSystem.domain.email.service.EmailService;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.ErrorType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmailController.class)
public class EmailControllerTest extends ApiTestSupport {

    @MockBean
    private EmailService emailService;

    @Test
    @WithMockUser
    public void 이메일_중복검증_중복아닐시() throws Exception {
        // given
        EmailRequest request = EmailFixture.emailRequest();
        doNothing().when(emailService)
            .checkDuplicatedEmail(any(EmailRequest.class));
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/email/check-duplicate")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("email/중복체크-성공",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void 이메일_중복검증_중복시() throws Exception {
        // given
        EmailRequest request = EmailFixture.emailRequest();
        doThrow(new CustomException(ErrorType.DUPLICATE_EMAIL_RESOURCE))
            .when(emailService)
            .checkDuplicatedEmail(any(EmailRequest.class));
        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/email/check-duplicate")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(request)))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcRestDocumentation.document("email/중복체크-중복",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
        // then
        resultActions.andExpect(status().isConflict()) // 409 Conflict
            .andExpect(
                jsonPath("$.httpStatusCode").value(
                    ErrorType.DUPLICATE_EMAIL_RESOURCE.getHttpStatusCode())) // 에러 코드 확인
            .andExpect(jsonPath("$.message").value(
                ErrorType.DUPLICATE_EMAIL_RESOURCE.getMessage())); // 에러 메시지 확인

    }

    @Test
    @WithMockUser
    public void 이메일_인증코드_발송() throws Exception {
        // given
        EmailRequest emailRequest = EmailFixture.emailRequest();
        doNothing()
            .when(emailService)
            .sendCodeToEmail(any(EmailRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/email/send-code")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(emailRequest)))
            .andDo(MockMvcRestDocumentation.document("email/인증코드-요청",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));
        // then
        resultActions.andExpect(status().isOk());
    }
}
