package com.example.rentalSystem.domain.facility.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.facility.convert.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update FACILITY set isDeleted = true where id=?")
@SQLRestriction("isDeleted = false")
@Table(name = "FACILITY")
public class Facility extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String location;

  @Column(nullable = false)
  private Long capacity;

  @Column(nullable = false)
  private String chargeProfessor;

  @Column(nullable = false)
  private boolean isAvailable;

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalTime endTime;

  @Convert(converter = StringListConverter.class)
  private List<String> possibleDays;

  @Convert(converter = StringListConverter.class)
  private List<String> supportFacilities;

  @Convert(converter = StringListConverter.class)
  private List<String> images;

  boolean isDeleted = false;

  @Builder
  public Facility(
      String name,
      String location,
      List<String> images,
      Long capacity,
      String chargeProfessor,
      LocalTime startTime,
      LocalTime endTime,
      List<String> supportFacilities,
      List<String> possibleDays,
      boolean isAvailable) {

    this.name = name;
    this.location = location;
    this.images = images;
    this.capacity = capacity;
    this.chargeProfessor = chargeProfessor;
    this.startTime = startTime;
    this.endTime = endTime;
    this.supportFacilities = supportFacilities;
    this.possibleDays = possibleDays;
    this.isAvailable = isAvailable;
  }

  public void update(Facility updateFacility) {
    this.name = updateFacility.getName();
    this.location = updateFacility.getLocation();
    this.images = updateFacility.getImages();
    this.capacity = updateFacility.getCapacity();
    this.chargeProfessor = updateFacility.getChargeProfessor();
    this.startTime = updateFacility.getStartTime();
    this.endTime = updateFacility.getEndTime();
    this.supportFacilities = updateFacility.getSupportFacilities();
    this.possibleDays = updateFacility.possibleDays;
    this.isAvailable = updateFacility.isAvailable();
  }
}
