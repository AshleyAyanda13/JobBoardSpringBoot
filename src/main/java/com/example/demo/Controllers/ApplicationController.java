package com.example.demo.Controller;

import com.example.demo.DTO.ApplicationDto;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/application")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationController(ApplicationService applicationService, UserRepository userRepository) {
        this.applicationService = applicationService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("User not authenticated");
        }

        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/applytoJob/{vacancyId}")
    public ResponseEntity<ApplicationDto> applyToJob(@PathVariable Long vacancyId,
                                                     @RequestBody ApplicationDto applicationDto) {
        User user = getAuthenticatedUser();
        ApplicationDto result = applicationService.applyToJob(user.getId(), vacancyId, applicationDto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationDto>> getApplicationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getApplicationsByUser(userId));
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @GetMapping("/application/{vacancyId}")
    public ResponseEntity<List<ApplicationDto>> getApplicantsforJob(@PathVariable Long vacancyId) {
        User recruiter = getAuthenticatedUser();
        return ResponseEntity.ok(applicationService.getApplicantsForJob(vacancyId, recruiter.getId()));
    }
}