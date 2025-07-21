package com.example.rentalSystem.domain.rentalHistory.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.rentalSystem.common.fixture.RentalFixture;
import com.example.rentalSystem.common.fixture.StudentFixture;
import com.example.rentalSystem.common.support.ApiTestSupport;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.example.rentalSystem.domain.rental.rentalhistory.controller.RentalController;
import com.example.rentalSystem.domain.rental.rentalhistory.dto.request.CreateRentalRequest;
import com.example.rentalSystem.domain.rental.rentalhistory.dto.response.RentalHistoryResponseDto;
import com.example.rentalSystem.domain.rental.rentalhistory.service.RentalService;
import com.example.rentalSystem.global.auth.security.CustomerDetails;
import com.example.rentalSystem.global.response.type.ResultType;
import com.example.rentalSystem.global.response.type.SuccessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RentalController.class)
public class RentalControllerTest extends ApiTestSupport {

    @MockBean
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        Student student = StudentFixture.createStudent();
        // 가짜 사용자 객체 생성 (CustomUserDetails를 구현한 객체)
        CustomerDetails userDetails = new CustomerDetails(student);

        // 인증 객체 생성
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void 학생_대여_신청() throws Exception {
        // Given
        CreateRentalRequest createRentalRequest = RentalFixture.createRentalRequest();

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/rental")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(toJson(createRentalRequest)))
            .andDo(MockMvcRestDocumentation.document("rental/대여 신청",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Then
        resultActions
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.CREATED.getHttpStatusCode()));
    }

    @Test
    void 대여_내역_전체_조회() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(1, 10);
        Page<RentalHistoryResponseDto> rentalHistoryResponseDtoList = RentalFixture.createAllRentalHistory(
            pageable);

        doReturn(rentalHistoryResponseDtoList).when(rentalService)
            .getAllRentalHistory(any(Pageable.class));

        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/rental")
                    .param("page", "1")
                    .param("size", "10")
                    .with(csrf()))
            .andDo(MockMvcRestDocumentation.document("rental/대여 내역 전체 조회",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Then
        resultActions
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.SUCCESS.getHttpStatusCode()));
    }
}
