package com.example.rentalSystem.domain.book.schedule.repository;

import com.example.rentalSystem.domain.book.schedule.entity.Schedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    //요청된 기간과 겹치는 모든 정기 스케줄을 가져오는 JPQL
    @Query("SELECT s FROM Schedule s " + "WHERE s.facility.id = :facilityId "
        + "AND s.dayOfWeek  = :dayOfWeek "
        + "AND s.validEndDate >= :validStartDate AND s.validStartDate <= :validEndDate")
    List<Schedule> findConflictsBy(
        @Param("facilityId") Long facilityId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("validStartDate") LocalDate validStartDate,
        @Param("validEndDate") LocalDate validEndDate);

    @Query("SELECT s FROM Schedule s " + "WHERE s.facility.id = :facilityId "
        + "AND s.dayOfWeek  = :dayOfWeek "
        + "AND s.rentalStartTime < :rentalEndTime AND s.rentalEndTime > :rentalStartTime")
    List<Schedule> findByFacilityIdAndDateAndBetweenTime(
        @Param("facilityId") Long facilityId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("rentalStartTime") LocalTime rentalStartTime,
        @Param("rentalEndTime") LocalTime rentalEndTime
    );

    @Query("SELECT s FROM Schedule s " + "WHERE s.facility.id = :facilityId "
        + "AND s.dayOfWeek = :dayOfWeek "
        + "AND s.validStartDate <= :validationDate AND s.validEndDate >= :validationDate")
    List<Schedule> findByFacilityIdAndDate(
        @Param("facilityId") Long facilityId,
        @Param("dayOfWeek") DayOfWeek dayOfWeek,
        @Param("validationDate") LocalDate validationDate
    );
}

