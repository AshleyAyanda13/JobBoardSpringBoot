package com.example.demo.Repository;


import com.example.demo.Models.Application;
import com.example.demo.Models.User;
import com.example.demo.Models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {


    List<Application> findByJob(Vacancy job);


    List<Application> findByApplicant(User applicant);
    List<Application> findByApplicantId(Long userId);


    Application findByJobAndApplicant(Vacancy job, User applicant);

    List<Application> findByJob_Id(Long vacancyId);

    Boolean existsByApplicantIdAndJobId (Long UseId,Long VacancyId );
}
