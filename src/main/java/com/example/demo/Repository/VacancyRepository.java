package com.example.demo.Repository;

import com.example.demo.Models.User;
import com.example.demo.Models.Vacancy;
import com.example.demo.Models.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long>, JpaSpecificationExecutor<Vacancy> {




    List<Vacancy> findByEmployer_Id(Long userId);

    Optional<Vacancy> findByIdAndEmployer_Id(Long id, Long employerId);


    void deleteByEmployer_Id(Long userId);
    List<Vacancy> findByCategory(String category);


    List<Vacancy> findByLocation(String location);


    List<Vacancy> findByDatePostedAfter(LocalDate date);


    List<Vacancy> findByEndDateAfter(LocalDate today);



}
