package com.example.rentalSystem.domain.suggestion.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSuggestion is a Querydsl query type for Suggestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSuggestion extends EntityPathBase<Suggestion> {

    private static final long serialVersionUID = -252627867L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSuggestion suggestion = new QSuggestion("suggestion");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    public final StringPath adminAnswer = createString("adminAnswer");

    public final DateTimePath<java.time.LocalDateTime> answeredAt = createDateTime("answeredAt", java.time.LocalDateTime.class);

    public final EnumPath<SuggestionCategory> category = createEnum("category", SuggestionCategory.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.example.rentalSystem.domain.facility.entity.QFacility facility;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<SuggestionStatus> status = createEnum("status", SuggestionStatus.class);

    public final com.example.rentalSystem.domain.member.student.entity.QStudent student;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSuggestion(String variable) {
        this(Suggestion.class, forVariable(variable), INITS);
    }

    public QSuggestion(Path<? extends Suggestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSuggestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSuggestion(PathMetadata metadata, PathInits inits) {
        this(Suggestion.class, metadata, inits);
    }

    public QSuggestion(Class<? extends Suggestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.facility = inits.isInitialized("facility") ? new com.example.rentalSystem.domain.facility.entity.QFacility(forProperty("facility")) : null;
        this.student = inits.isInitialized("student") ? new com.example.rentalSystem.domain.member.student.entity.QStudent(forProperty("student")) : null;
    }

}

