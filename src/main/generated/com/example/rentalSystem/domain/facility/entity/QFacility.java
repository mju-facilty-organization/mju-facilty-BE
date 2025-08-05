package com.example.rentalSystem.domain.facility.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFacility is a Querydsl query type for Facility
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFacility extends EntityPathBase<Facility> {

    private static final long serialVersionUID = -424365085L;

    public static final QFacility facility = new QFacility("facility");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    public final ListPath<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType, EnumPath<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType>> allowedBoundary = this.<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType, EnumPath<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType>>createList("allowedBoundary", com.example.rentalSystem.domain.member.base.entity.type.AffiliationType.class, EnumPath.class, PathInits.DIRECT2);

    public final NumberPath<Long> capacity = createNumber("capacity", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created_at = _super.created_at;

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final StringPath facilityNumber = createString("facilityNumber");

    public final EnumPath<com.example.rentalSystem.domain.facility.entity.type.FacilityType> facilityType = createEnum("facilityType", com.example.rentalSystem.domain.facility.entity.type.FacilityType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> images = this.<String, StringPath>createList("images", String.class, StringPath.class, PathInits.DIRECT2);

    public final BooleanPath isAvailable = createBoolean("isAvailable");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final ListPath<com.example.rentalSystem.domain.book.schedule.entity.Schedule, com.example.rentalSystem.domain.book.schedule.entity.QSchedule> schedules = this.<com.example.rentalSystem.domain.book.schedule.entity.Schedule, com.example.rentalSystem.domain.book.schedule.entity.QSchedule>createList("schedules", com.example.rentalSystem.domain.book.schedule.entity.Schedule.class, com.example.rentalSystem.domain.book.schedule.entity.QSchedule.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final ListPath<String, StringPath> supportFacilities = this.<String, StringPath>createList("supportFacilities", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public QFacility(String variable) {
        super(Facility.class, forVariable(variable));
    }

    public QFacility(Path<? extends Facility> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFacility(PathMetadata metadata) {
        super(Facility.class, metadata);
    }

}

