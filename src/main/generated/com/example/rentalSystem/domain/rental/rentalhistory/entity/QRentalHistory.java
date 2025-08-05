package com.example.rentalSystem.domain.rental.rentalhistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRentalHistory is a Querydsl query type for RentalHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRentalHistory extends EntityPathBase<RentalHistory> {

    private static final long serialVersionUID = 192876653L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRentalHistory rentalHistory = new QRentalHistory("rentalHistory");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> defineDateTime = createDateTime("defineDateTime", java.time.LocalDateTime.class);

    public final com.example.rentalSystem.domain.facility.entity.QFacility facility;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> numberOfPeople = createNumber("numberOfPeople", Integer.class);

    public final StringPath organization = createString("organization");

    public final com.example.rentalSystem.domain.member.pic.entity.QPic pic;

    public final StringPath purpose = createString("purpose");

    public final StringPath reason = createString("reason");

    public final EnumPath<RentalApplicationResult> rentalApplicationResult = createEnum("rentalApplicationResult", RentalApplicationResult.class);

    public final DateTimePath<java.time.LocalDateTime> rentalEndDateTime = createDateTime("rentalEndDateTime", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> rentalStartDateTime = createDateTime("rentalStartDateTime", java.time.LocalDateTime.class);

    public final com.example.rentalSystem.domain.member.student.entity.QStudent student;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRentalHistory(String variable) {
        this(RentalHistory.class, forVariable(variable), INITS);
    }

    public QRentalHistory(Path<? extends RentalHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRentalHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRentalHistory(PathMetadata metadata, PathInits inits) {
        this(RentalHistory.class, metadata, inits);
    }

    public QRentalHistory(Class<? extends RentalHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.facility = inits.isInitialized("facility") ? new com.example.rentalSystem.domain.facility.entity.QFacility(forProperty("facility")) : null;
        this.pic = inits.isInitialized("pic") ? new com.example.rentalSystem.domain.member.pic.entity.QPic(forProperty("pic")) : null;
        this.student = inits.isInitialized("student") ? new com.example.rentalSystem.domain.member.student.entity.QStudent(forProperty("student")) : null;
    }

}

