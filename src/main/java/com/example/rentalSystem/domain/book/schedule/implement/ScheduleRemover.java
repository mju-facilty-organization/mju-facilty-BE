// com.example.rentalSystem.domain.book.schedule.implement.ScheduleRemover

package com.example.rentalSystem.domain.book.schedule.implement;

import com.example.rentalSystem.domain.book.schedule.repository.ScheduleRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduleRemover {

  private final ScheduleRepository scheduleRepository;

  @Transactional
  public void deleteFacilityOrganizationRange(Long facilityId, String organization,
      LocalDate start, LocalDate end) {
    scheduleRepository.deleteByFacilityOrgAndOverlap(facilityId, organization, start, end);
  }
}