package com.example.rentalSystem.domain.facility.entity;

import com.example.rentalSystem.domain.common.BaseTimeEntity;
import com.example.rentalSystem.domain.facility.convert.StringListConverter;
import com.example.rentalSystem.domain.facility.convert.WeekScheduleListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update facility set is_deleted = true where id=?")
@SQLRestriction("is_deleted = false")
@Table(name = "facility")
public class Facility extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @Column(nullable = false)
    private Long capacity;

    @Column(nullable = false)
    private String allowedBoundary;

    @Convert(converter = StringListConverter.class)
    private List<String> supportFacilities;

    @Column
    private String pic; // 책임자

    @Convert(converter = WeekScheduleListConverter.class)
    private Map<String, List<String>> possibleTimes;

    @Column
    private boolean isAvailable;

    @Builder
    public Facility(
        String name,
        String location,
        List<String> images,
        Long capacity,
        String allowedBoundary,
        List<String> supportFacilities,
        String pic,
        Map<String, List<String>> possibleTimes,
        boolean isAvailable) {

        this.name = name;
        this.location = location;
        this.images = images;
        this.capacity = capacity;
        this.allowedBoundary = allowedBoundary;
        this.supportFacilities = supportFacilities;
        this.pic = pic;
        this.possibleTimes = possibleTimes;
        this.isAvailable = isAvailable;
    }

    public void update(Facility updateFacility) {
        this.name = updateFacility.getName();
        this.location = updateFacility.getLocation();
        this.images = updateFacility.getImages();
        this.capacity = updateFacility.getCapacity();
        this.supportFacilities = updateFacility.getSupportFacilities();
        this.isAvailable = updateFacility.isAvailable();
    }
}
