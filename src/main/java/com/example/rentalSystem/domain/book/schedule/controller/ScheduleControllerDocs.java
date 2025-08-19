package com.example.rentalSystem.domain.book.schedule.controller;

import com.example.rentalSystem.domain.book.schedule.dto.request.CreateRegularScheduleRequest;
import com.example.rentalSystem.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "스케줄 관리 API", description = "시설 스케줄을 관리하는 API입니다.")
@RequestMapping(value = "/admin/schedules", produces = "application/json;charset=utf-8")
public interface ScheduleControllerDocs {

  @Operation(summary = "정기 스케줄 생성", description = "정기 시설 스케줄을 생성합니다.")
  @PostMapping("/regular")
  ApiResponse<?> createRegularFacility(
      @Parameter(description = "정기 스케줄 생성 요청 데이터", required = true)
      @Valid @RequestBody CreateRegularScheduleRequest request
  );

  @Operation(
      summary = "스케줄 엑셀 업로드",
      description = """
          시간표 엑셀(xls/xlsx)을 업로드합니다.
          - 기본적으로 같은 강좌번호(organization) + 같은 시설(facility) + 유효기간이 겹치는 기존 스케줄은 overwrite=true일 때 삭제 후 재생성합니다.
          - overwrite=false이면 기존 스케줄과 겹치면 충돌로 처리합니다.
          """
  )
  @PostMapping(path = "/upload-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ApiResponse<?> uploadExcel(
      @Parameter(
          description = "업로드할 엑셀 파일(xls/xlsx)",
          required = true,
          content = @Content(
              mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
              schema = @Schema(type = "string", format = "binary")
          )
      )
      @RequestParam("file") MultipartFile file,

      @Parameter(description = "유효 시작일 (YYYY-MM-DD). 미지정 시 서버 기본값 사용.")
      @RequestParam(required = false) LocalDate validStartDate,

      @Parameter(description = "유효 종료일 (YYYY-MM-DD). 미지정 시 서버 기본값 사용.")
      @RequestParam(required = false) LocalDate validEndDate,

      @Parameter(
          description = "덮어쓰기 여부. true면 같은 (facility, organization) + 기간 겹침 데이터는 삭제 후 재생성.",
          example = "true"
      )
      @RequestParam(defaultValue = "true", value = "overwrite") boolean overwrite
  );
}