package com.example.rentalSystem.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.rentalSystem.domain.member.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.member.base.entity.Member;
import com.example.rentalSystem.domain.member.base.entity.type.Role;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 486125457L;

    public static final QMember member = new QMember("member1");

    public final EnumPath<AffiliationType> college = createEnum("college", AffiliationType.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

