package com.example.rentalSystem.global.initializer;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.member.entity.Role;
import com.example.rentalSystem.domain.pic.entity.Pic;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.global.exception.custom.CustomException;
import com.example.rentalSystem.global.response.type.ErrorType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityStructure {

    private static final int INFO_COUNT = 6;
    private static final int column = 1;
    private PasswordEncoder passwordEncoder;

    public Pic pic() {
        return Pic.builder()
            .phoneNumber("010-1111-2222")
            .password(passwordEncoder.encode("picPassword"))
            .college(AffiliationType.ICT.getName())
            .email("pic@a.com")
            .name("교직원")
            .role(Role.PIC)
            .build();
    }

    public Set<Professor> professors() {
        List<String> data = CsvReader.readCSV("src/main/resources/professor.csv");

        return extractProfessor(data);

    }

    private Set<Professor> extractProfessor(List<String> data) {
        return data.stream()
            .skip(column)
            .map(this::convertToProfessor)
            .collect(Collectors.toSet());
    }

    private Professor convertToProfessor(String line) {
        String[] professorInfo = splitItems(line);
        if (professorInfo.length < INFO_COUNT) {
            throw new CustomException(ErrorType.FAIL_READ_CSV);
        }
        return Professor
            .builder()
            .id(Integer.parseInt(professorInfo[0]))
            .name(professorInfo[1])
            .campusType(AffiliationType.getInstance(professorInfo[2]))
            .college(AffiliationType.getInstance(professorInfo[3]))
            .major(AffiliationType.getInstance(professorInfo[4]))
            .email(professorInfo[5])
            .build();
    }

    private String[] splitItems(String input) {
        return Arrays.stream(input.split(","))
            .map(String::trim)
            .toArray(String[]::new);
    }

}
