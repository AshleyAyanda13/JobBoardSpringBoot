package com.example.demo.Services;

import com.example.demo.DTO.EducationDto;
import com.example.demo.DTO.WorkExperienceDto;
import com.example.demo.Models.Education;
import com.example.demo.Models.User;
import com.example.demo.Models.WorkExperience;
import com.example.demo.Repository.EducationRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class EducationService {
    public EducationDto mapToDTO(Education education) {
        EducationDto dto = new EducationDto();

        dto.setInstitution(education.getInstitution());
        dto.setDegree(education.getDegree());
        dto.setStartDate(education.getStartDate());
        dto.setEndDate(education.getEndDate());
        dto.setFieldOfStudy(education.getFieldOfStudy());
        return dto;
    }
    public Education mapToEntity(EducationDto educationDto) {
        Education education = new Education();

        education.setInstitution(educationDto.getInstitution());
        education.setDegree(educationDto.getDegree());
        education.setStartDate(educationDto.getStartDate());
        education.setEndDate(educationDto.getEndDate());
        education.setFieldOfStudy(educationDto.getFieldOfStudy());
        return education;
    }
    private final EducationRepository educationRepository;

private  final UserRepository userRepository;
    EducationService (EducationRepository educationRepository,UserRepository userRepository)
    {
        this.educationRepository=educationRepository;
        this.userRepository=userRepository;
    }
@Transactional
    public Education addEducation(Long userId, EducationDto educationDto) {
        if (educationDto == null) {
            throw new IllegalArgumentException("Education DTO cannot be null");
        }

         User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    if (educationRepository.existsByUser_Id(userId)) {
        throw new IllegalStateException("User already has a Education.");
    }

        Education education = mapToEntity(educationDto);

        education.setUser(user);

        return educationRepository.save(education);
    }

    public EducationDto GetUsersEducation(Long UserId)
    {

        Education education = educationRepository
                .findByUser_Id(UserId)
                .orElseThrow(() -> new EntityNotFoundException("Education not found for user ID: " + UserId));

        return mapToDTO(education);

    }
    @Transactional
    public EducationDto UpdateUsersEducation(Long UserId,  EducationDto updatedEducationDto)
    {

        Education existingEducation = educationRepository
                .findByUser_Id(UserId)
                .orElseThrow(() -> new EntityNotFoundException("Education not found for user ID: " + UserId));

        existingEducation.setInstitution(updatedEducationDto.getInstitution());
        existingEducation.setDegree(updatedEducationDto.getDegree());
        existingEducation.setStartDate(updatedEducationDto.getStartDate());
        existingEducation.setEndDate(updatedEducationDto.getEndDate());
        existingEducation.setFieldOfStudy(updatedEducationDto.getFieldOfStudy());

        Education savedWorkExperience = educationRepository.save(existingEducation);
        return mapToDTO(savedWorkExperience);


    }
    @Transactional
    public void DeleteUsersEducation(Long UserId)
    {


        educationRepository.deleteByUser_Id(UserId);
    }




}
