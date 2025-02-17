package com.example.rentalSystem.domain.rentalhistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProfessorHistory is a Querydsl query type for ProfessorHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfessorHistory extends EntityPathBase<ProfessorHistory> {

    private static final long serialVersionUID = -1010651086L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProfessorHistory professorHistory = new QProfessorHistory("professorHistory");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created_at = _super.created_at;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.rentalSystem.domain.professor.entity.QProfessor professor;

    public final StringPath reason = createString("reason");

    public final EnumPath<RentalApplicationResult> rentalApplicationResult = createEnum("rentalApplicationResult", RentalApplicationResult.class);

    public final QRentalHistory rentalHistory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public QProfessorHistory(String variable) {
        this(ProfessorHistory.class, forVariable(variable), INITS);
    }

    public QProfessorHistory(Path<? extends ProfessorHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProfessorHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProfessorHistory(PathMetadata metadata, PathInits inits) {
        this(ProfessorHistory.class, metadata, inits);
    }

    public QProfessorHistory(Class<? extends ProfessorHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.professor = inits.isInitialized("professor") ? new com.example.rentalSystem.domain.professor.entity.QProfessor(forProperty("professor")) : null;
        this.rentalHistory = inits.isInitialized("rentalHistory") ? new QRentalHistory(forProperty("rentalHistory"), inits.get("rentalHistory")) : null;
    }

}

