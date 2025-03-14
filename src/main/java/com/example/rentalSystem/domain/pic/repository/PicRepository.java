package com.example.rentalSystem.domain.pic.repository;

import com.example.rentalSystem.domain.pic.entity.Pic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PicRepository extends JpaRepository<Pic, Long> {

}
