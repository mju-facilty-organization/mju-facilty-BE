package com.example.rentalSystem.domain.facility.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.facility.convert.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
}
