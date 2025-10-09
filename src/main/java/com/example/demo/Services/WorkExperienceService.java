package com.example.demo.Services;

import com.example.demo.DTO.WorkExperienceDto;
import com.example.demo.Models.WorkExperience;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.ManyToOne;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkExperienceService {
    @Autowired
    private com.example.demo.Repository.WorkExperienceRepository workExperienceRepository;

    @Autowired
    private com.example.demo.Repository.UserRepository userRepository;





    public WorkExperienceDto mapToDTO(WorkExperience experience) {
       WorkExperienceDto dto =new WorkExperienceDto();
        dto.setCompany(experience.getCompany());
        dto.setPosition(experience.getPosition());
        dto.setStartDate(experience.getStartDate());
        dto.setEndDate(experience.getEndDate());
        dto.setDescription(experience.getDescription());



        return dto;
    }
    public WorkExperience mapToEntity(WorkExperienceDto Wedto) {
        WorkExperience WorkExp=new WorkExperience();
        WorkExp.setCompany(Wedto.getCompany());
        WorkExp.setPosition(Wedto.getPosition());
        WorkExp.setStartDate(LocalDate.now());
        WorkExp.setEndDate(Wedto.getEndDate());
        WorkExp.setDescription(Wedto.getDescription());

        return WorkExp;
    }





    public WorkExperienceDto getWorkExperienceByUserId(Long userId) {
        WorkExperience workExperience = workExperienceRepository
                .findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Work experience not found for user ID: " + userId));

        return mapToDTO(workExperience);
    }

@Transactional
    public WorkExperience addWorkExperience(Long userId, WorkExperienceDto wexperienceDto) {
        if (wexperienceDto == null) {
            throw new IllegalArgumentException("WorkExperienceDto cannot be null");
        }

        com.example.demo.Models.User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

    if (workExperienceRepository.existsByUser_Id(userId)) {
        throw new IllegalStateException("User already has a work experience entry.");
    }

    WorkExperience workExperience = mapToEntity(wexperienceDto);
        workExperience.setUser(user);

        return workExperienceRepository.save(workExperience);
    }
@Transactional
    public WorkExperienceDto updateWorkExperience(Long userId, WorkExperienceDto updatedWorkExpDto) {
        WorkExperience existingWorkExperience = workExperienceRepository
                .findByUser_Id(userId)
                .orElseThrow(() -> new EntityNotFoundException("Work experience not found for user ID: " + userId));

        existingWorkExperience.setCompany(updatedWorkExpDto.getCompany());
        existingWorkExperience.setPosition(updatedWorkExpDto.getPosition());
        existingWorkExperience.setStartDate(updatedWorkExpDto.getStartDate());
        existingWorkExperience.setEndDate(updatedWorkExpDto.getEndDate());
        existingWorkExperience.setDescription(updatedWorkExpDto.getDescription());

        WorkExperience savedWorkExperience = workExperienceRepository.save(existingWorkExperience);
        return mapToDTO(savedWorkExperience);
    }

@Transactional
    public void deleteWorkExperinceByUserID(Long UserId) {
        workExperienceRepository.deleteByUser_Id(UserId);
    }
}
