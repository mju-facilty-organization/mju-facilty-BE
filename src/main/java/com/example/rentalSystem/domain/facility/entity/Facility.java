package com.example.rentalSystem.domain.facility.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.common.convert.AffiliationListConverter;
import com.example.rentalSystem.domain.common.convert.FacilityTypeConverter;
import com.example.rentalSystem.domain.common.convert.StringListConverter;
import com.example.rentalSystem.domain.facility.entity.type.FacilityType;
import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
//@SQLDelete(sql = "update facility set is_deleted = true where id=?")
@SQLRestriction("is_deleted = false")
@Table(
        name = "facility",
        uniqueConstraints = @UniqueConstraint(columnNames = {"facility_number"})
)

public class Facility extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = FacilityTypeConverter.class)
    private FacilityType facilityType;

    @Column(nullable = false)
    private String facilityNumber;

    @Lob
    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @Column(nullable = false)
    private Long capacity;

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
    @Convert(converter = AffiliationListConverter.class)
    private List<AffiliationType> allowedBoundary;

    @Builder
    public Facility(
            String facilityType,
            String facilityNumber,
            List<String> images,
            Long capacity,
            List<String> supportFacilities,
            LocalTime startTime,
            LocalTime endTime,
            List<AffiliationType> allowedBoundary,
            boolean isAvailable) {
        this.facilityType = FacilityType.getInstanceByValue(facilityType);
        this.facilityNumber = facilityNumber;
        this.images = images;
        this.capacity = capacity;
        this.supportFacilities = supportFacilities;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.allowedBoundary = allowedBoundary;
        this.isDeleted = false;
    }

    public void updateAll(
            FacilityType newType,           // 키 변경: null 이면 유지
            String newNumber,               // 키 변경: null 이면 유지
            Long capacity,                  // null 유지
            LocalTime startTime,            // null 유지
            LocalTime endTime,              // null 유지
            List<String> supportFacilities, // null 유지
            List<AffiliationType> boundary, // null 유지
            Boolean available               // null 유지
    ) {
        if (newType != null) {
            this.facilityType = newType;
        }
        if (newNumber != null && !newNumber.isBlank()) {
            this.facilityNumber = newNumber;
        }
        if (capacity != null) {
            this.capacity = capacity;
        }
        if (startTime != null) {
            this.startTime = startTime;
        }
        if (endTime != null) {
            this.endTime = endTime;
        }
        if (supportFacilities != null) {
            this.supportFacilities = supportFacilities;
        }
        if (boundary != null) {
            this.allowedBoundary = boundary;
        }
        if (available != null) {
            this.isAvailable = available;
        }
    }
    
    public void replaceImages(List<String> newImages) {
        this.images = (newImages == null) ? new java.util.ArrayList<>() : new java.util.ArrayList<>(newImages);
    }

}
