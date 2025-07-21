package com.example.rentalSystem.domain.professor.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.member.professor.entity.Professor;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfessor is a Querydsl query type for Professor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfessor extends EntityPathBase<Professor> {

    private static final long serialVersionUID = 128783459L;

    public static final QProfessor professor = new QProfessor("professor");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    public final EnumPath<AffiliationType> campusType = createEnum("campusType", AffiliationType.class);

    public final EnumPath<AffiliationType> college = createEnum("college", AffiliationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> created_at = _super.created_at;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<AffiliationType> major = createEnum("major", AffiliationType.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updated_at = _super.updated_at;

    public QProfessor(String variable) {
        super(Professor.class, forVariable(variable));
    }

    public QProfessor(Path<? extends Professor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfessor(PathMetadata metadata) {
        super(Professor.class, metadata);
    }

}

