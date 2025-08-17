package com.example.rentalSystem.domain.book.rentalhistory.repository;

import com.example.rentalSystem.domain.book.rentalhistory.entity.RentalHistory;
import com.example.rentalSystem.domain.book.rentalhistory.entity.type.RentalApplicationResult;
import com.example.rentalSystem.domain.member.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RentalHistoryRepository extends JpaRepository<RentalHistory, Long> {

    List<RentalHistory> findAllByStudent(Student student);

    @Query("SELECT r FROM RentalHistory r " + "WHERE r.facility.id = :facilityId "
            + "AND r.rentalStartDate  = :startDate "
            + "AND r.rentalEndTime >= :startTime AND r.rentalStartTime <= :endTime")
    List<RentalHistory> findByFacilityIdAndDateAndBetweenTime(
            @Param("facilityId") Long facilityId,
            @Param("startDate") LocalDate startDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT rh FROM RentalHistory rh " + "WHERE rh.facility.id = :facilityId "
            + "AND rh.rentalStartDate <= :localDate AND rh.rentalEndDate >= :localDate")
    List<RentalHistory> findByFacilityIdAndDate(
            @Param("facilityId") Long facilityId,
            @Param("localDate") LocalDate localDate);

    @Query("""
                SELECT r
                FROM RentalHistory r
                JOIN FETCH r.student s
                WHERE r.facility.id = :facilityId
                  AND (r.rentalStartDate < :today OR (r.rentalStartDate = :today AND r.rentalStartTime <= :nowTime))
                  AND (r.rentalEndDate   > :today OR (r.rentalEndDate   = :today AND r.rentalEndTime   >= :nowTime))
                  AND r.rentalApplicationResult = :status
                ORDER BY r.rentalStartDate, r.rentalStartTime
            """)
    List<RentalHistory> findCurrentlyInUseByFacility(
            @Param("facilityId") Long facilityId,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime,
            @Param("status") RentalApplicationResult status
    );
}
