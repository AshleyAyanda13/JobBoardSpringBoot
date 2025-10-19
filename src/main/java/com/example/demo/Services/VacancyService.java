package com.example.demo.Services;

import com.example.demo.DTO.VacancyDto;
import com.example.demo.DTO.VacancySearchCriteriaDto;
import com.example.demo.Models.User;
import com.example.demo.Models.Vacancy;
import com.example.demo.Models.WorkExperience;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.VacancyRepository;
import com.example.demo.Specifications.VacancySpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.ManyToOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service


public class VacancyService {
    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private UserRepository userRepository;


    public VacancyDto mapToDTO(Vacancy job) {
        VacancyDto dto = new VacancyDto();
        dto.setId(job.getId());
        dto.setJobTitle(job.getJobTitle());
        dto.setJobDescription(job.getJobDescription());
        dto.setDatePosted(job.getDatePosted());
        dto.setEndDate(job.getEndDate());
        dto.setLocation(job.getLocation());
        dto.setCategory(job.getCategory());
        dto.setSalary(job.getSalary());

        return dto;
    }
    public Vacancy mapToEntity(VacancyDto jobDto) {
        Vacancy vacancy = new Vacancy();

        vacancy.setJobTitle(jobDto.getJobTitle());
        vacancy.setJobDescription(jobDto.getJobDescription());
        vacancy.setDatePosted(LocalDate.now());
        vacancy.setEndDate(jobDto.getEndDate());
        vacancy.setLocation(jobDto.getLocation());
        vacancy.setCategory(jobDto.getCategory());

        vacancy.setSalary(jobDto.getSalary());

        return vacancy;
    }



    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }



    public VacancyDto getVacancyById(Long id,Long UserId) {

        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        User user = userRepository.findById(UserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + UserId));

        return mapToDTO(vacancy);
    }
@Transactional
    public VacancyDto createVacancy(Long userId, VacancyDto vacancyDto) {
        if (vacancyDto == null) {
            throw new IllegalArgumentException("VacancyDto cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Vacancy vacancy = mapToEntity(vacancyDto);
        vacancy.setEmployer(user);

        vacancy.setLastUpdatedDate(LocalDate.now());

        Vacancy savedVacancy = vacancyRepository.save(vacancy);
        return mapToDTO(savedVacancy);
    }

    public List<VacancyDto> getVacanciesByUser(Long userId) {
        List<Vacancy> vacancies = vacancyRepository.findByEmployer_Id(userId);
        return vacancies.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public VacancyDto updateVacancy(Long userId, Long vacancyId, VacancyDto updatedVacancyDto) {
        if (updatedVacancyDto == null) {
            throw new IllegalArgumentException("VacancyDto cannot be null");
        }

        Vacancy vacancy = vacancyRepository.findByIdAndEmployer_Id(vacancyId, userId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found for this employer"));


        vacancy.setJobTitle(updatedVacancyDto.getJobTitle());
        vacancy.setJobDescription(updatedVacancyDto.getJobDescription());
        vacancy.setEndDate(updatedVacancyDto.getEndDate());
        vacancy.setLocation(updatedVacancyDto.getLocation());
        vacancy.setCategory(updatedVacancyDto.getCategory());
        vacancy.setSalary(updatedVacancyDto.getSalary());
        vacancy.setLastUpdatedDate(LocalDate.now());

        Vacancy savedVacancy = vacancyRepository.save(vacancy);
        return mapToDTO(savedVacancy);
    }

@Transactional
    public void deleteVacancy(Long userId, Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findByIdAndEmployer_Id(vacancyId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Vacancy not found for this employer"));

        vacancyRepository.delete(vacancy);
    }
    public List<VacancyDto> searchVacancies(VacancySearchCriteriaDto criteria) {
        Specification<Vacancy> spec = null;

        boolean hasKeyword = criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty();
        boolean hasLocation = criteria.getLocation() != null && !criteria.getLocation().trim().isEmpty();

        if (hasKeyword) {
            spec = Specification.where(VacancySpecifications.hasKeyword(criteria.getKeyword().trim()));
        }

        if (hasLocation) {
            spec = (spec == null)
                    ? Specification.where(VacancySpecifications.hasLocation(criteria.getLocation().trim()))
                    : spec.and(VacancySpecifications.hasLocation(criteria.getLocation().trim()));
        }

        List<Vacancy> results = (spec == null)
                ? vacancyRepository.findAll()
                : vacancyRepository.findAll(spec);

        return results.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

}
