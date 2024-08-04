//package com.example.rentalSystem.domain.facility.entity;
//
//import com.example.rentalSystem.domain.common.BaseTimeEntity;
//import com.example.rentalSystem.domain.facility.convert.StringListConverter;
//import jakarta.persistence.Column;
//import jakarta.persistence.Convert;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import java.util.ArrayList;
//import java.util.List;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//public class Facility extends BaseTimeEntity {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//
//  @Column(nullable = false)
//  private String name;
//
//  @Column(nullable = false)
//  private String location;
//
//  @Convert(converter = StringListConverter.class)
//  private List<String> images;
//
//  @Column(nullable = false)
//  private Long capacity;
//}
