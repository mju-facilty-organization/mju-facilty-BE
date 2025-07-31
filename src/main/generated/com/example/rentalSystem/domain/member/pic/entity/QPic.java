package com.example.rentalSystem.domain.member.pic.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPic is a Querydsl query type for Pic
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPic extends EntityPathBase<Pic> {

    private static final long serialVersionUID = 1449119959L;

    public static final QPic pic = new QPic("pic");

    public final com.example.rentalSystem.domain.member.base.entity.QMember _super = new com.example.rentalSystem.domain.member.base.entity.QMember(this);

    //inherited
    public final EnumPath<com.example.rentalSystem.domain.member.base.entity.type.AffiliationType> college = _super.college;

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final StringPath password = _super.password;

    //inherited
    public final StringPath phoneNumber = _super.phoneNumber;

    //inherited
    public final EnumPath<com.example.rentalSystem.domain.member.base.entity.type.Role> role = _super.role;

    public QPic(String variable) {
        super(Pic.class, forVariable(variable));
    }

    public QPic(Path<? extends Pic> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPic(PathMetadata metadata) {
        super(Pic.class, metadata);
    }

}

