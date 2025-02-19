package com.example.rentalSystem.domain.affiliation.type;

import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AffiliationType {
    SEOUL(0, "캠퍼스", "서울", null),

    // 총학
    STUDENT_COUNCIL(1, "총학", "총학생회", SEOUL),

    // 인문대
    HUMANITIES(2, "단과대", "인문대학", null),
    KOREAN_LANGUAGE_AND_LITERATURE(3, "학과", "국어국문학과", HUMANITIES),
    CREATIVE_WRITING(4, "학과", "문예창작학과", HUMANITIES),
    ENGLISH_LANGUAGE_AND_LITERATURE(5, "학과", "영어영문학과", HUMANITIES),
    CHINESE_LANGUAGE_AND_LITERATURE(6, "학과", "중어중문학과", HUMANITIES),
    JAPANESE_LANGUAGE_AND_LITERATURE(7, "학과", "일어일문학과", HUMANITIES),
    LIBRARY_AND_INFORMATION_SCIENCE(8, "학과", "문헌정보학과", HUMANITIES),
    ART_HISTORY(9, "학과", "미술사학과", HUMANITIES),
    ARABIC_STUDIES(10, "학과", "아랍지역학과", HUMANITIES),
    HISTORY(11, "학과", "사학과", HUMANITIES),
    PHILOSOPHY(12, "학과", "철학과", HUMANITIES),

    // 사회과학대
    SOCIAL_SCIENCE(13, "단과대", "사회과학대학", null),
    PUBLIC_ADMINISTRATION(14, "학과", "행정학과", SOCIAL_SCIENCE),
    ECONOMICS(15, "학과", "경제학과", SOCIAL_SCIENCE),
    POLITICAL_SCIENCE_AND_DIPLOMACY(16, "학과", "정치외교학과", SOCIAL_SCIENCE),
    DIGITAL_MEDIA(17, "학과", "디지털미디어학과", SOCIAL_SCIENCE),
    CHILD_STUDIES(18, "학과", "아동학과", SOCIAL_SCIENCE),
    YOUTH_GUIDANCE(19, "학과", "청소년지도학과", SOCIAL_SCIENCE),

    // 경영대
    BUSINESS(20, "단과대", "경영대학", null),
    BUSINESS_ADMINISTRATION(21, "학과", "경영학과", BUSINESS),
    MANAGEMENT_INFORMATION_SYSTEMS(22, "학과", "경영정보학과", BUSINESS),
    INTERNATIONAL_TRADE(23, "학과", "국제통상학과", BUSINESS),

    // 법대
    LAW(24, "단과대", "법학대학", null),
    DEPARTMENT_OF_LAW(25, "학과", "법학과", LAW),

    // ICT융합대
    ICT(26, "단과대", "ICT융합대학", null),
    DIGITAL_CONTENTS_DESIGN(27, "학과", "디지털콘텐츠디자인학과", ICT),
    SOFTWARE_APPLICATIONS(28, "학과", "응용소프트웨어전공", ICT),
    DATA_TECHNOLOGY(29, "학과", "데이터테크놀로지전공", ICT);

    private final int val;
    private final String councilType;
    private final String name;
    private final AffiliationType parent;  // 상위 단과대

    public static AffiliationType getInstance(String name) {
        return Arrays.stream(values())
            .filter(type -> type.name.equals(name))
            .findFirst()
            .orElseThrow(
                () -> new CustomException(ErrorType.INVALID_AFFILIATION_TYPE)); // 없으면 예외 발생
    }

    public static AffiliationType getCollegeByMajor(String major) {
        return Arrays.stream(values())
            .filter(type -> type.name.equals(major))
            .findFirst()
            .orElseThrow(
                () -> new CustomException(ErrorType.INVALID_AFFILIATION_TYPE)); // 없으면 예외 발생
    }

    public static List<AffiliationType> getChildList(String affiliationTypeString) {
        AffiliationType parent = getInstance(affiliationTypeString);
        return Arrays.stream(AffiliationType.values())
            .filter(type -> type.getParent() != null && type.getParent().equals(parent))
            .toList();
    }
}