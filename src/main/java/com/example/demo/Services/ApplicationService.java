package com.example.demo.Service;

import com.example.demo.DTO.ApplicationDto;
import com.example.demo.Models.Application;
import com.example.demo.Models.Resume;
import com.example.demo.Models.User;
import com.example.demo.Models.Vacancy;
import com.example.demo.Repository.ApplicationRepository;
import com.example.demo.Repository.ResumeRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.VacancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;
    private final ResumeRepository resumeRepository;

    public ApplicationService(ApplicationRepository applicationRepository,
                              UserRepository userRepository,
                              VacancyRepository vacancyRepository,
                              ResumeRepository resumeRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.vacancyRepository = vacancyRepository;
        this.resumeRepository = resumeRepository;
    }
@Transactional
    public ApplicationDto applyToJob(Long applicantId, Long vacancyId, ApplicationDto applicationDto) {
        User applicant = userRepository.findById(applicantId)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        Vacancy job = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));



        Resume resume = resumeRepository.findByOwnerId(applicantId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        boolean alreadyApplied = applicationRepository.existsByApplicantIdAndJobId(applicantId,vacancyId);
        if (alreadyApplied) {
            throw new RuntimeException("You have already applied to this job.");
        }

        Application application = new Application();
        application.setApplicant(applicant);
        application.setJob(job);
        application.setResume(resume);
        application.setAppliedDate(LocalDate.now());
        application.setCoverletter(applicationDto.getCoverletter());
        Application saved = applicationRepository.save(application);

        return mapToDto(saved);
    }
    @Transactional

    public List<ApplicationDto> getApplicationsByUser(Long userId) {
        return applicationRepository.findByApplicantId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    public List<ApplicationDto> SearchMethod(Long userId) {
        return applicationRepository.findByApplicantId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    @Transactional

    public List<ApplicationDto> getApplicantsForJob(Long vacancyId, Long posterId) throws RuntimeException {
        Vacancy vacancy = vacancyRepository.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        if (!vacancy.getEmployer().getId().equals(posterId)) {


            throw new RuntimeException("You don't own this job posting");
        }

        List<Application> applications = applicationRepository.findByJob_Id(vacancyId);
        return applications.stream().map(this::mapToDto).toList();
    }


    private ApplicationDto mapToDto(Application application) {
        User applicant = application.getApplicant();
        Vacancy job = application.getJob();
        Resume resume = application.getResume();

        ApplicationDto dto = new ApplicationDto();
        dto.setAppliedDate(application.getAppliedDate());
        dto.setApplicantId(applicant.getId());
        dto.setApplicantName(applicant.getName());
        dto.setApplicantSurname(applicant.getSurname());
        dto.setApplicantEmail(applicant.getEmail());
        dto.setApplicantPhone(applicant.getPhoneNumber());
        dto.setVacancyId(job.getId());
        dto.setJobTitle(job.getJobTitle());
        dto.setResumeId(resume != null ? resume.getId() : null);
        dto.setCoverletter(application.getCoverletter());

        return dto;
    }
}
