package com.example.rentalSystem.global.initializer;

import com.example.rentalSystem.domain.affiliation.type.AffiliationType;
import com.example.rentalSystem.domain.member.entity.Role;
import com.example.rentalSystem.domain.pic.entity.Pic;
import com.example.rentalSystem.domain.pic.repository.PicRepository;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.professor.repository.ProfessorRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private PicRepository picRepository;
    private ProfessorRepository professorRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (picRepository.count() == 0) {
            Pic pic = Pic.builder()
                .phoneNumber("010-1111-2222")
                .password(passwordEncoder.encode("picPassword"))
                .college(AffiliationType.ICT.getName())
                .email("pic@a.com")
                .name("교직원")
                .role(Role.PIC)
                .build();
            picRepository.save(pic);
        }
        if (professorRepository.count() == 0) {
            Professor professor = Professor.builder()
                .campusType(AffiliationType.SEOUL)
                .college(AffiliationType.ICT)
                .major(AffiliationType.SOFTWARE_APPLICATIONS)
                .email("professor@e.com")
                .name("최성운")
                .build();
            professorRepository.save(professor);
        }
    }
}
