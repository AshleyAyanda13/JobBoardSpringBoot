package com.example.demo.Controller;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.ApplicationDto;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
private final JwtUtil jwtUtil;
private final UserRepository userRepository;

    public ApplicationController(ApplicationService applicationService,JwtUtil jwtUtil,UserRepository userRepository) {
        this.applicationService = applicationService;
        this.jwtUtil=jwtUtil;
        this.userRepository=userRepository;
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/applytoJob")
    public ApplicationDto applyToJob(@RequestParam Long vacancyId,@RequestBody ApplicationDto applicationDto, HttpServletRequest request) {


        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return applicationService.applyToJob(user.getId(), vacancyId,applicationDto);
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public List<ApplicationDto> getApplicationsByUser(@PathVariable Long userId) {
        return applicationService.getApplicationsByUser(userId);
    }
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @GetMapping("/application/{vacancyId}")
    public List<ApplicationDto> getApplicantsforJob( HttpServletRequest request,@PathVariable Long vacancyId) {

        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return applicationService.getApplicantsForJob(vacancyId,user.getId());
    }
}
