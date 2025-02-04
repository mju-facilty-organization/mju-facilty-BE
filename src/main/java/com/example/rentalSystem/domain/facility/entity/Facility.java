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
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
//@SQLDelete(sql = "update facility set is_deleted = true where id=?")
@SQLRestriction("is_deleted = false")
@Table(name = "facility")
public class Facility extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String facilityType;

    @Column(nullable = false)
    private String facilityNumber;

    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @Column(nullable = false)
    private Long capacity;

    @Column(nullable = false)
    private String allowedBoundary;

    @Convert(converter = StringListConverter.class)
    private List<String> supportFacilities;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @Column
    private boolean isAvailable;

    @Column
    private boolean isDeleted;

    @Column
    private String pic; // 책임자

    @Builder
    public Facility(
        String facilityType,
        String facilityNumber,
        List<String> images,
        Long capacity,
        String allowedBoundary,
        List<String> supportFacilities,
        String pic,
        LocalTime startTime,
        LocalTime endTime,
        boolean isAvailable) {
        this.facilityType = FacilityType.existsByValue(facilityType);
        this.facilityNumber = facilityNumber;
        this.images = images;
        this.capacity = capacity;
        this.allowedBoundary = allowedBoundary;
        this.supportFacilities = supportFacilities;
        this.pic = pic;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.isDeleted = false;
    }

    public void update(Facility updateFacility) {
        this.facilityType = updateFacility.getFacilityType();
        this.facilityNumber = updateFacility.getFacilityNumber();
        this.images = updateFacility.getImages();
        this.capacity = updateFacility.getCapacity();
        this.supportFacilities = updateFacility.getSupportFacilities();
        this.isAvailable = updateFacility.isAvailable();
    }
}
