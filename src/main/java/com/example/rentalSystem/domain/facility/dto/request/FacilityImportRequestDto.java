package com.example.rentalSystem.domain.facility.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FacilityImportRequestDto {
    private MultipartFile file;
    private boolean dryRun;
    private boolean overwrite;
}