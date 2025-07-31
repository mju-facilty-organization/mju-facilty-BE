package com.example.rentalSystem.domain.rental.approval.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProfessorApproval is a Querydsl query type for ProfessorApproval
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfessorApproval extends EntityPathBase<ProfessorApproval> {

    private static final long serialVersionUID = 276812840L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProfessorApproval professorApproval = new QProfessorApproval("professorApproval");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created_at = _super.created_at;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.rentalSystem.domain.member.professor.entity.QProfessor professor;

    public final StringPath reason = createString("reason");

    public final EnumPath<com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalApplicationResult> rentalApplicationResult = createEnum("rentalApplicationResult", com.example.rentalSystem.domain.rental.rentalhistory.entity.RentalApplicationResult.class);

    public final com.example.rentalSystem.domain.rental.rentalhistory.entity.QRentalHistory rentalHistory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public QProfessorApproval(String variable) {
        this(ProfessorApproval.class, forVariable(variable), INITS);
    }

    public QProfessorApproval(Path<? extends ProfessorApproval> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProfessorApproval(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProfessorApproval(PathMetadata metadata, PathInits inits) {
        this(ProfessorApproval.class, metadata, inits);
    }

    public QProfessorApproval(Class<? extends ProfessorApproval> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.professor = inits.isInitialized("professor") ? new com.example.rentalSystem.domain.member.professor.entity.QProfessor(forProperty("professor")) : null;
        this.rentalHistory = inits.isInitialized("rentalHistory") ? new com.example.rentalSystem.domain.rental.rentalhistory.entity.QRentalHistory(forProperty("rentalHistory"), inits.get("rentalHistory")) : null;
    }

}

