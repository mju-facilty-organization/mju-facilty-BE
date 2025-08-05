package com.example.rentalSystem.domain.book.schedule.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSchedule is a Querydsl query type for Schedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSchedule extends EntityPathBase<Schedule> {

    private static final long serialVersionUID = -319089232L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSchedule schedule = new QSchedule("schedule");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created_at = _super.created_at;

    public final EnumPath<java.time.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", java.time.DayOfWeek.class);

    public final com.example.rentalSystem.domain.facility.entity.QFacility facility;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath organization = createString("organization");

    public final TimePath<java.time.LocalTime> rentalEndTime = createTime("rentalEndTime", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> rentalStartTime = createTime("rentalStartTime", java.time.LocalTime.class);

    public final EnumPath<com.example.rentalSystem.domain.facility.entity.type.ScheduleType> scheduleType = createEnum("scheduleType", com.example.rentalSystem.domain.facility.entity.type.ScheduleType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public final DatePath<java.time.LocalDate> validEndDate = createDate("validEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> validStartDate = createDate("validStartDate", java.time.LocalDate.class);

    public QSchedule(String variable) {
        this(Schedule.class, forVariable(variable), INITS);
    }

    public QSchedule(Path<? extends Schedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSchedule(PathMetadata metadata, PathInits inits) {
        this(Schedule.class, metadata, inits);
    }

    public QSchedule(Class<? extends Schedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.facility = inits.isInitialized("facility") ? new com.example.rentalSystem.domain.facility.entity.QFacility(forProperty("facility")) : null;
    }

}

