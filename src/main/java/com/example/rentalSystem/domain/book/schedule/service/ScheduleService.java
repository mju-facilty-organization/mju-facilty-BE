package com.example.rentalSystem.domain.book.schedule.service;

import com.example.rentalSystem.domain.book.schedule.dto.request.CreateRegularScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.dto.request.ScheduleRequest;
import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.book.schedule.implement.ScheduleReader;
import com.example.rentalSystem.domain.book.schedule.implement.ScheduleSaver;
import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.implement.FacilityImpl;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final FacilityImpl facilityImpl;
    private final ScheduleReader scheduleReader;
    private final ScheduleSaver scheduleSaver;

    @Transactional
    public void createSchedule(CreateRegularScheduleRequest request) {
        Facility facility = facilityImpl.findById(request.facilityId());
        checkInvalidDateRange(request);
        validateAndCheckConflicts(facility, request);
        List<Schedule> newSchedules = request.toEntities(facility);
        scheduleSaver.saveAll(newSchedules);
    }

    private void checkInvalidDateRange(CreateRegularScheduleRequest request) {
        if (request.validStartDate().isAfter(request.validEndDate())) {
            throw new CustomException(ErrorType.INVALID_DATE_RANGE);
        }
    }

    private void validateAndCheckConflicts(Facility facility,
        CreateRegularScheduleRequest request) {
        // 모든 요청에 대한 기존 스케줄을 미리 한 번에 조회
        for (ScheduleRequest scheduleDto : request.schedules()) {
            DayOfWeek dayOfWeek = scheduleDto.dayOfWeek();
            LocalTime rentalStartTime = scheduleDto.rentalStartTime();
            LocalTime rentalEndTime = scheduleDto.rentalEndTime();

            if (rentalStartTime.isAfter(rentalEndTime)) {
                throw new CustomException(ErrorType.INVALID_TIME_RANGE);
            }

            List<Schedule> existingSchedules = scheduleReader.getSchedulesInBoundary(
                facility.getId(), dayOfWeek, request.validStartDate(), request.validEndDate());

            for (Schedule existingSchedule : existingSchedules) {
                if (isTimeOverlap(rentalStartTime, rentalEndTime,
                    existingSchedule.getRentalStartTime(),
                    existingSchedule.getRentalEndTime())) {
                    throw new CustomException(ErrorType.SCHEDULE_CONFLICT);
                }
            }
            //Booking 엔티티와의 충돌 확인 로직도 유사하게 추가 가능
        }
    }

    private boolean isTimeOverlap(LocalTime newStartTime, LocalTime newEndTime,
        LocalTime existingStartTime, LocalTime existingEndTime) {
        return newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime);
    }
}
