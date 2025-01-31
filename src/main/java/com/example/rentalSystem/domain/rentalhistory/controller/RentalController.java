package com.example.rentalSystem.domain.rentalhistory.controller;

import com.example.rentalSystem.domain.member.entity.CustomerDetails;
import com.example.rentalSystem.domain.rentalhistory.dto.CreateRentalRequest;
import com.example.rentalSystem.domain.rentalhistory.service.RentalService;
import com.example.rentalSystem.global.response.ApiResponse;
import com.example.rentalSystem.global.response.SuccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rental")
public class RentalController {

    final RentalService rentalService;

    @PostMapping()
    public ApiResponse<?> createRental(
        @AuthenticationPrincipal CustomerDetails customerDetails,
        @RequestBody CreateRentalRequest createRentalRequest) {
        rentalService.create(customerDetails.getMember(), createRentalRequest);
        return ApiResponse.success(SuccessType.CREATED);
    }
}
