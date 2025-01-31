package com.example.rentalSystem.domain.facility.reposiotry;

import com.example.rentalSystem.domain.facility.entity.Facility;
import com.example.rentalSystem.domain.facility.entity.TimeTable;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    Optional<TimeTable> findByFacilityAndDate(Facility facility, LocalDate date);

    void findByFacilityAndDate(Facility facility, org.joda.time.LocalDate localDate);
}
