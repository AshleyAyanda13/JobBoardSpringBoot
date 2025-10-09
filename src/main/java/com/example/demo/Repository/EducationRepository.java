package com.example.demo.Repository;

import com.example.demo.Models.Education;
import com.example.demo.Models.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationRepository extends JpaRepository<Education,Long> {

    Optional<Education> findByUser_Id(Long userId);
    void deleteByUser_Id(Long userId);
Boolean existsByUser_Id(Long userId);
}
