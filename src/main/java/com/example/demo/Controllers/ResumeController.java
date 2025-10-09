package com.example.demo.Controllers;

import com.example.demo.DTO.ResumeDto;
import com.example.demo.Services.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    @Autowired
    private final com.example.demo.Config.JwtUtil jwtUtil;
private final com.example.demo.Repository.UserRepository userRepository;
    public ResumeController(ResumeService resumeService, com.example.demo.Config.JwtUtil jwtUtil, com.example.demo.Repository.UserRepository userRepository) {
        this.resumeService = resumeService;
        this.jwtUtil=jwtUtil;
        this.userRepository=userRepository;
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/upload")
    public ResumeDto uploadResume(HttpServletRequest request,
                                  @RequestParam("file") MultipartFile file) throws IOException {


        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return resumeService.uploadResume(user.getId(), file);
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/user/{userId}")
    public ResumeDto getResumesByUser(@PathVariable Long userId) {
        return resumeService.getResumeByUser(userId);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadOwnResume(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ResumeDto dto = resumeService.getResumeByUser(user.getId());


        byte[] data = resumeService.getResumeFileData(user.getId());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getFileName() + "\"")
                .body(data);

    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> DeleteCV(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));




       resumeService.deleteResume(user.getId());
        com.example.demo.DTO.ResponseMessage responseMessage=new com.example.demo.DTO.ResponseMessage();
        responseMessage.Message="Deletion Successful";
        return ResponseEntity.ok(responseMessage.toString());


    }
}

