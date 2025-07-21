package com.example.rentalSystem.domain.student.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.rentalSystem.domain.member.base.entity.type.AffiliationType;
import com.example.rentalSystem.domain.member.base.entity.type.Role;
import com.example.rentalSystem.domain.member.student.entity.Student;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStudent is a Querydsl query type for Student
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudent extends EntityPathBase<Student> {

    private static final long serialVersionUID = 1895145443L;

    public static final QStudent student = new QStudent("student");

    public final com.example.rentalSystem.domain.member.entity.QMember _super = new com.example.rentalSystem.domain.member.entity.QMember(this);

    //inherited
    public final EnumPath<AffiliationType> college = _super.college;

    //inherited
    public final StringPath email = _super.email;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final EnumPath<AffiliationType> major = createEnum("major", AffiliationType.class);

    //inherited
    public final StringPath name = _super.name;

    //inherited
    public final StringPath password = _super.password;

    //inherited
    public final StringPath phoneNumber = _super.phoneNumber;

    //inherited
    public final EnumPath<Role> role = _super.role;

    public final StringPath studentNumber = createString("studentNumber");

    public final NumberPath<Long> warningTime = createNumber("warningTime", Long.class);

    public QStudent(String variable) {
        super(Student.class, forVariable(variable));
    }

    public QStudent(Path<? extends Student> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStudent(PathMetadata metadata) {
        super(Student.class, metadata);
    }

}

