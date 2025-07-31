package com.example.rentalSystem.domain.facility.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.rentalSystem.common.fixture.FacilityFixture;
import com.example.rentalSystem.common.support.ApiTestSupport;
import com.example.rentalSystem.domain.facility.dto.request.CreateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.request.UpdateFacilityRequestDto;
import com.example.rentalSystem.domain.facility.dto.response.PreSignUrlListResponse;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.service.FacilityService;
import com.example.rentalSystem.global.response.type.ResultType;
import com.example.rentalSystem.global.response.type.SuccessType;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FacilityController.class)
class FacilityControllerTest extends ApiTestSupport {


    @MockBean
    private FacilityService facilityService;

    @Test
    @WithMockUser
    @DisplayName("시설을 생성할 수 있다.")
    void 시설_생성_API() throws Exception {
        // Given
        CreateFacilityRequestDto requestDto = FacilityFixture.createFacilityRequestDto();
        PreSignUrlListResponse presignUrlListResponse = FacilityFixture.createFacilityResponseDto();

        doReturn(presignUrlListResponse)
            .when(facilityService)
            .create(any(CreateFacilityRequestDto.class));
        // When
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/admin/facilities")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(requestDto)));
        // Then
        resultActions
            .andExpect(jsonPath("$.resultType").value(String.valueOf(ResultType.SUCCESS)))
            .andExpect(jsonPath("$.httpStatusCode").value(SuccessType.CREATED.getHttpStatusCode()));
    }

    @Test
    @DisplayName("시설을 수정할 수 있다.")
    void 시설_수정_API() throws Exception {
        // Given
        Long facilityId = 1L;
        Facility facility = FacilityFixture.createFacility();
//        facilityJpaRepository.save(facility);

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
//        facilityJpaRepository.save(facility);
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
    @WithMockUser
    void 시설_전체_조회() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        doReturn(FacilityFixture.getAllFacilityList(pageable))
            .when(facilityService)
            .getAll(any(Pageable.class), any(String.class));

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/admin/facilities")
                .param("page", "1")
                .param("size", "10")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 타입별_시설_전체_조회() throws Exception {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        doReturn(FacilityFixture.getAllFacilityList(pageable))
            .when(facilityService)
            .getAll(any(Pageable.class), any(String.class));

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/admin/facilities")
                .param("page", "1")
                .param("size", "10")
                .param("facility_type", "본관")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 시설_상세_조회() throws Exception {
        //given
        Long facilityId = 1L;

        doReturn(FacilityFixture.getFacilityDetail())
            .when(facilityService)
            .getFacilityDetail(any(Long.class), any(LocalDate.class));

        //when
        ResultActions resultActions = mockMvc.perform(
            get("/admin/facilities/{facilityId}", facilityId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .param("date", LocalDate.now().toString()));
        resultActions.andExpect(status().isOk());
    }
}
