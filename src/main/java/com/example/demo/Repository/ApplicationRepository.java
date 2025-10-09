package com.example.demo.Repository;


import com.example.demo.Models.Application;
import com.example.demo.Models.User;
import com.example.demo.Models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Find all applications by a specific job
    List<Application> findByJob(Vacancy job);

    // Find all applications submitted by a specific user
    List<Application> findByApplicant(User applicant);
    List<Application> findByApplicantId(Long userId);

    // Optional: Find application by job and applicant (to prevent duplicate applications)
    Application findByJobAndApplicant(Vacancy job, User applicant);

    List<Application> findByJob_Id(Long vacancyId);

    Boolean existsByApplicantIdAndJobId (Long UseId,Long VacancyId );
}
