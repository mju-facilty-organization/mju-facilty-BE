package com.example.rentalSystem.domain.facility.presentation.controller;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.rentalSystem.common.fixture.FacilityFixture;
import com.example.rentalSystem.common.support.ApiTestSupport;
import com.example.rentalSystem.domain.facility.controller.FacilityController;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.FacilityResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.reposiotry.FacilityJpaRepository;
import com.example.rentalSystem.global.response.ResultType;
import com.example.rentalSystem.global.response.SuccessType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FacilityController.class)
class FacilityControllerTest extends ApiTestSupport {

    @Autowired
    private FacilityJpaRepository facilityJpaRepository;

    @Test
    @DisplayName("시설을 생성할 수 있다.")
    void 시설_생성_API() throws Exception {
        // Given
        CreateFacilityRequestDto requestDto = FacilityFixture.createFacilityRequestDto();

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/admin/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestDto)))
            .andDo(MockMvcRestDocumentation.document("facility/시설등록",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())));

        // Then
        resultActions
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.CREATED.getHttpStatusCode()));
    }

    @Test
    @DisplayName("시설을 수정할 수 있다.")
    void 시설_수정_API() throws Exception {
        // Given
        Long facilityId = 1L;
        Facility facility = FacilityFixture.createFacility();
        facilityJpaRepository.save(facility);

        UpdateFacilityRequestDto requestDto = FacilityFixture.createUpdateFacilityRequestDto();

        // When
        ResultActions resultActions = mockMvc.perform(
            put("/api/admin/facilities/{facilityId}", facilityId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestDto)));

        // Then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.SUCCESS.getHttpStatusCode()));
    }

    @Test
    @DisplayName("시설을 삭제할 수 있다.")
    void 시설_삭제_API() throws Exception {
        // Given
        Long facilityId = 1L;
        Facility facility = FacilityFixture.createFacility();
        facilityJpaRepository.save(facility);
        // When
        ResultActions resultActions = mockMvc.perform(
            delete("/api/admin/facilities/{facilityId}", facilityId));

        // Then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.SUCCESS.getHttpStatusCode()));
    }

    @Test
    @DisplayName("시설을 전체 조회할 수 있다.")
    void 시설_전체조회_API() throws Exception {
        // Given
        Facility facility = FacilityFixture.createFacility();
        Facility facility1 = FacilityFixture.createUpdateFacility();
        facilityJpaRepository.save(facility);
        facilityJpaRepository.save(facility1);

        List<FacilityResponse> facilityResponses = new ArrayList<>(List.of(
            FacilityResponse.fromFacility(facility),
            FacilityResponse.fromFacility(facility1)
        ));
        // When
        ResultActions resultActions = mockMvc.perform(get("/api/admin/facilities"));

        // Then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.SUCCESS.getHttpStatusCode()))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(facilityResponses.size()));
    }
}
