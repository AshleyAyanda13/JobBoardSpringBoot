package com.example.demo.Controllers;

import com.example.demo.DTO.EducationDto;
import com.example.demo.DTO.ResponseMessage;
import com.example.demo.DTO.UserInfoDto;
import com.example.demo.DTO.WorkExperienceDto;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Services.CustomUserDetailsService;
import com.example.demo.Services.EducationService;
import com.example.demo.Services.WorkExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class JobSeekerBackgroundController {

    private final WorkExperienceService workExperienceService;
    private final EducationService educationService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    @Autowired
    public JobSeekerBackgroundController(
            EducationService educationService,
            WorkExperienceService workExperienceService,
            UserRepository userRepository,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.workExperienceService = workExperienceService;
        this.educationService = educationService;
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("User not authenticated");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/myworkExperience")
    public ResponseEntity<List<WorkExperienceDto>> GetMyWorkExperience() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(workExperienceService.getWorkExperienceByUserId(user.getId()));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/myworkExperience/{Id}")
    public ResponseEntity<WorkExperienceDto> GetSingleWorkExperience(@PathVariable Long Id) {
        getAuthenticatedUser(); // Just to validate access
        return ResponseEntity.ok(workExperienceService.getWorkExperience(Id));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/AddMyWorkExperience")
    public ResponseEntity<WorkExperienceDto> AddMyWorkExperience(@RequestBody WorkExperienceDto workExperienceDto) {
        User user = getAuthenticatedUser();
        workExperienceService.addWorkExperience(user.getId(), workExperienceDto);
        return ResponseEntity.ok(workExperienceDto);
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PutMapping("/updateUserWorkExperience/{Id}")
    public ResponseEntity<String> UpdateUsersExperience(@RequestBody WorkExperienceDto workExperienceDto, @PathVariable Long Id) {
        User user = getAuthenticatedUser();
        workExperienceService.updateWorkExperience(user.getId(), Id, workExperienceDto);
        return ResponseEntity.ok("WorkExperience Updated Successfully");
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @DeleteMapping("/DeleteUsersWorkExperience/{Id}")
    public ResponseEntity<String> DeleteMyExperience(@PathVariable Long Id) {
        User user = getAuthenticatedUser();
        workExperienceService.deleteWorkExperinceByUserID(user.getId(), Id);
        return ResponseEntity.ok("WorkExperience Deleted Successfully");
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/myEducation")
    public ResponseEntity<List<EducationDto>> GetMyEducation() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(educationService.GetUsersEducation(user.getId()));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/myEducation/{Id}")
    public ResponseEntity<EducationDto> GetSingleEducation(@PathVariable Long Id) {
        getAuthenticatedUser(); // Just to validate access
        return ResponseEntity.ok(educationService.GetSingleEducation(Id));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/AddMyEducation")
    public ResponseEntity<EducationDto> AddMyEducation(@RequestBody EducationDto educationDto) {
        User user = getAuthenticatedUser();
        educationService.addEducation(user.getId(), educationDto);
        return ResponseEntity.ok(educationDto);
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PutMapping("/updateUserEducation/{Id}")
    public ResponseEntity<String> UpdateUsersEducation(@PathVariable Long Id, @RequestBody EducationDto educationDto) {
        User user = getAuthenticatedUser();
        educationService.UpdateUsersEducation(user.getId(), Id, educationDto);
        return ResponseEntity.ok("Education Updated Successfully");
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @DeleteMapping("/DeleteUsersEducation/{Id}")
    public ResponseEntity<String> DeleteUsersEducation(@PathVariable Long Id) {
        User user = getAuthenticatedUser();
        educationService.DeleteUsersEducation(user.getId(), Id);
        return ResponseEntity.ok("Education Details Deleted Successfully");
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/LoggedInUser")
    public ResponseEntity<UserInfoDto> GetLoggedInUserDetails() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(customUserDetailsService.showUserInformation(user.getUsername()));
    }
}