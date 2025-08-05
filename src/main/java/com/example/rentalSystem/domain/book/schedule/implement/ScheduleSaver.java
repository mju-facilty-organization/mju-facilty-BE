package com.example.rentalSystem.domain.book.schedule.implement;

import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import com.example.rentalSystem.domain.book.schedule.repository.ScheduleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduleSaver {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void saveAll(List<Schedule> newSchedules) {
        scheduleRepository.saveAll(newSchedules);
    }
}
