//package com.example.rentalSystem.domain.rentalhistory.entity;
//
//import com.example.rentalSystem.domain.common.BaseTimeEntity;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import java.time.LocalDateTime;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//public class RentalHistory extends BaseTimeEntity {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//
//  @Column(nullable = false)
//  private String purpose;
//
//  @Column(nullable = false)
//  private String organization;
//
//  @Column(nullable = false)
//  private LocalDateTime rentalStartDate;
//
//  @Column(nullable = false)
//  private LocalDateTime rentalEndDate;
//
//  @Column(nullable = false)
//  private RentalApplicationResult result;
//}
