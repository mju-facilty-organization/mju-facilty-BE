package com.example.rentalSystem.domain.facility.entity.timeTable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTimeTable is a Querydsl query type for TimeTable
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTimeTable extends EntityPathBase<TimeTable> {

    private static final long serialVersionUID = -618040556L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTimeTable timeTable = new QTimeTable("timeTable");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final com.example.rentalSystem.domain.facility.entity.QFacility facility;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final MapPath<java.time.LocalTime, TimeStatus, EnumPath<TimeStatus>> timeSlot = this.<java.time.LocalTime, TimeStatus, EnumPath<TimeStatus>>createMap("timeSlot", java.time.LocalTime.class, TimeStatus.class, EnumPath.class);

    public QTimeTable(String variable) {
        this(TimeTable.class, forVariable(variable), INITS);
    }

    public QTimeTable(Path<? extends TimeTable> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTimeTable(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTimeTable(PathMetadata metadata, PathInits inits) {
        this(TimeTable.class, metadata, inits);
    }

    public QTimeTable(Class<? extends TimeTable> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.facility = inits.isInitialized("facility") ? new com.example.rentalSystem.domain.facility.entity.QFacility(forProperty("facility")) : null;
    }

}

