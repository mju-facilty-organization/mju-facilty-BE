package com.example.rentalSystem.global.scheduler;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.timeTable.TimeTable;
import com.example.rentalSystem.domain.facility.implement.FacilityReader;
import com.example.rentalSystem.domain.facility.implement.FacilitySaver;
import com.example.rentalSystem.domain.facility.reposiotry.TimeTableRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FacilityScheduler {

    private final FacilityReader facilityReader;
    private final TimeTableRepository tableRepository;

    private final int batchSize = 100;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyTimeTables() {
        List<Facility> facilities = facilityReader.getAll();
        LocalDate today = LocalDate.now();
        List<TimeTable> timeTableList = new ArrayList<>();

        for (Facility facility : facilities) {
            TimeTable timeTable = TimeTable.toEntity(facility, today, facility.getStartTime(),
                facility.getEndTime());
            timeTableList.add(timeTable);

            if (timeTableList.size() >= batchSize) {
                tableRepository.saveAll(timeTableList);
                timeTableList.clear();
            }
        }
        if (!timeTableList.isEmpty()) {
            tableRepository.saveAll(timeTableList);
        }
    }
}
