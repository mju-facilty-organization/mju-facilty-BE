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
@Table(name = "facility")
public class Facility extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = FacilityTypeConverter.class)
    private FacilityType facilityType;

    @Column(nullable = false)
    private String facilityNumber;

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

    public void update(Facility updateFacility) {
        this.facilityType = updateFacility.getFacilityType();
        this.facilityNumber = updateFacility.getFacilityNumber();
        this.images = updateFacility.getImages();
        this.capacity = updateFacility.getCapacity();
        this.supportFacilities = updateFacility.getSupportFacilities();
        this.isAvailable = updateFacility.isAvailable();
    }

    // 기존값이랑 겹칠 때 갱신용
    public void updateMeta(long capacity, LocalTime start, LocalTime end,
                           List<String> supports, List<AffiliationType> boundary, boolean available) {
        this.capacity = capacity;
        this.startTime = start;
        this.endTime = end;
        this.supportFacilities = supports;
        this.allowedBoundary = boundary;
        this.isAvailable = available;
    }

}
