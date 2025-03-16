package com.example.rentalSystem.global.initializer;

import com.example.rentalSystem.domain.pic.entity.Pic;
import com.example.rentalSystem.domain.pic.repository.PicRepository;
import com.example.rentalSystem.domain.professor.entity.Professor;
import com.example.rentalSystem.domain.professor.repository.ProfessorRepository;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private PicRepository picRepository;
    private ProfessorRepository professorRepository;
    private EntityStructure entityStructure;


    @Override
    public void run(String... args) throws Exception {
        if (picRepository.count() == 0) {
            Pic pic = entityStructure.pic();
            picRepository.save(pic);
        }
        if (professorRepository.count() == 0) {
            Set<Professor> professors = entityStructure.professors("professor.csv");
            professorRepository.saveAll(professors);
        }

    }
}
