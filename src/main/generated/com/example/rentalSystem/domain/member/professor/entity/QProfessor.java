package com.example.rentalSystem.domain.member.professor.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfessor is a Querydsl query type for Professor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfessor extends EntityPathBase<Professor> {

    private static final long serialVersionUID = 2063813303L;

    public static final QProfessor professor = new QProfessor("professor");

    public final com.example.rentalSystem.domain.common.QBaseTimeEntity _super = new com.example.rentalSystem.domain.common.QBaseTimeEntity(this);

    public final EnumPath<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType> campusType = createEnum("campusType", com.example.rentalSystem.domain.member.base.entity.type.AffiliationType.class);

    public final EnumPath<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType> college = createEnum("college", com.example.rentalSystem.domain.member.base.entity.type.AffiliationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType> major = createEnum("major", com.example.rentalSystem.domain.member.base.entity.type.AffiliationType.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

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

