package com.example.rentalSystem.domain.book.schedule.importer.mapper;

import com.example.rentalSystem.domain.book.schedule.dto.request.CreateRegularScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.dto.request.ExcelScheduleRow;
import com.example.rentalSystem.domain.book.schedule.dto.request.ScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.importer.util.DayOfWeekResolver;
import com.example.rentalSystem.domain.facility.entity.type.ScheduleType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ScheduleRowMapper {

  public CreateRegularScheduleRequest toCreateRequest(
      ExcelScheduleRow row, Long facilityId, LocalDate validStart, LocalDate validEnd
  ) {
    ScheduleRequest sr = new ScheduleRequest(
        DayOfWeekResolver.resolve(row.day()),
        LocalTime.parse(row.startTime()),
        LocalTime.parse(row.endTime()),
        row.subject(),
        row.professor(),
        row.capacity()
    );

    return new CreateRegularScheduleRequest(
        row.courseCode(),       // organization: 강좌번호 사용
        ScheduleType.CLASS,
        facilityId,
        List.of(sr),
        validStart,
        validEnd
    );
  }
}