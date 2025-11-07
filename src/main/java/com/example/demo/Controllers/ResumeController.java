package com.example.demo.Controllers;

import com.example.demo.DTO.ResumeDto;
import com.example.demo.DTO.ResponseMessage;
import com.example.demo.Models.Resume;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Services.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepository;

    @Autowired
    public ResumeController(ResumeService resumeService, UserRepository userRepository) {
        this.resumeService = resumeService;
        this.userRepository = userRepository;
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
    @PostMapping("/upload")
    public ResumeDto uploadResume(@RequestParam("file") MultipartFile file) throws IOException {
        User user = getAuthenticatedUser();
        return resumeService.uploadResume(user.getId(), file);
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResumeDto getResumesByUser(@PathVariable Long userId) {
        return resumeService.getResumeByUser(userId);
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @GetMapping("/resume/{Id}")
    public ResponseEntity<byte[]> getResumesById(@PathVariable Long Id) {
        Resume resume = resumeService.getResumeById(Id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resume.getData());
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @GetMapping("/resumedata/{Id}")
    public ResponseEntity<ResumeDto> getResumeDataById(@PathVariable Long Id) {
        return ResponseEntity.ok(resumeService.getResumeDataById(Id));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadOwnResume() {
        User user = getAuthenticatedUser();
        ResumeDto dto = resumeService.getResumeByUser(user.getId());
        byte[] data = resumeService.getResumeFileData(user.getId());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getFileName() + "\"")
                .body(data);
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCV() {
        User user = getAuthenticatedUser();
        resumeService.deleteResume(user.getId());

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.Message = "Deletion Successful";
        return ResponseEntity.ok(responseMessage.toString());
    }
}