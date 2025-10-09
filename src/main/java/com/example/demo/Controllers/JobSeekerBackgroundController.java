package com.example.demo.Controllers;
import com.example.demo.DTO.EducationDto;
import com.example.demo.DTO.ResponseMessage;
import com.example.demo.DTO.WorkExperienceDto;
import com.example.demo.Services.EducationService;
import com.example.demo.Services.WorkExperienceService;
import jakarta.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class JobSeekerBackgroundController {
@Autowired
    private final WorkExperienceService workExperienceService;

private final EducationService educationService;
    private final com.example.demo.Config.JwtUtil jwtUtil;

    private final com.example.demo.Repository.UserRepository userRepository;
@Autowired
    JobSeekerBackgroundController(EducationService educationService,WorkExperienceService workExperienceService, com.example.demo.Config.JwtUtil jwtUtil, com.example.demo.Repository.UserRepository userRepository)
    {
       this.workExperienceService=workExperienceService;
        this.jwtUtil=jwtUtil;
        this.userRepository=userRepository;
        this.educationService=educationService;
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/myworkExperience")
    public ResponseEntity<WorkExperienceDto> GetMyWorkExperience(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return ResponseEntity.ok(workExperienceService.getWorkExperienceByUserId(user.getId()));


    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/AddMyWorkExperience")
    public ResponseEntity<WorkExperienceDto> AddMyWorkExperience(HttpServletRequest request,@RequestBody WorkExperienceDto workExperienceDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        workExperienceService.addWorkExperience(user.getId(),workExperienceDto);

        return ResponseEntity.ok(workExperienceDto);


    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PutMapping("/updateUserWorkExperience")
    public ResponseEntity<String> UpdateUsersExperience(HttpServletRequest request,@RequestBody WorkExperienceDto workExperienceDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
workExperienceService.updateWorkExperience(user.getId(),workExperienceDto);
        ResponseMessage responseMessage =new ResponseMessage();
        return ResponseEntity.ok(responseMessage.Message="WorkExperience Updated Successfully");



    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @DeleteMapping("/DeleteUsersWorkExperience")
    public ResponseEntity<String> DeleteMyExperience(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        workExperienceService.deleteWorkExperinceByUserID(user.getId());
        ResponseMessage responseMessage =new ResponseMessage();
        return ResponseEntity.ok(responseMessage.Message="WorkExperience Deleted Successfully"

        );


    }


    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/myEducation")
    public ResponseEntity<EducationDto> GetMyEducaton(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return ResponseEntity.ok(educationService.GetUsersEducation(user.getId()));


    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/AddMyEducation")
    public ResponseEntity<EducationDto> AddMyEducation(HttpServletRequest request,@RequestBody EducationDto educationDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        educationService.addEducation(user.getId(),educationDto);

        return ResponseEntity.ok(educationDto);


    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PutMapping("/updateUserEducation")
    public ResponseEntity<String> UpdateUsersEducation(HttpServletRequest request,@RequestBody EducationDto educationDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        educationService.UpdateUsersEducation(user.getId(),educationDto);
        ResponseMessage responseMessage =new ResponseMessage();
        return ResponseEntity.ok(responseMessage.Message="Education Updated Successfully");



    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @DeleteMapping("/DeleteUsersEducation")
    public ResponseEntity<String> DeleteUsersEducation(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        educationService.DeleteUsersEducation(user.getId());
        ResponseMessage responseMessage =new ResponseMessage();
        return ResponseEntity.ok(responseMessage.Message="Education Details Deleted Successfully"

        );


    }









}
