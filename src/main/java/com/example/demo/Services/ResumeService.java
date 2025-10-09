package com.example.demo.Services;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.DTO.ResumeDto;
import com.example.demo.Models.Resume;
import com.example.demo.Models.User;
import com.example.demo.Repository.ResumeRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
    }
    @Transactional

    public ResumeDto uploadResume(Long userId, MultipartFile file) throws IOException {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Resume> existingResume = resumeRepository.findByOwnerId(userId);

        Resume resume = existingResume.orElse(new Resume());

        resume.setOwner(owner);
        resume.setFileName(file.getOriginalFilename());
        resume.setFileType(file.getContentType());
        resume.setData(file.getBytes());
        resume.setDateUploaded(LocalDate.now());

        Resume saved = resumeRepository.save(resume);
        return mapToDto(saved);
    }
    @Transactional
    public byte[] getResumeFileData(Long userid) {
        Resume resume = resumeRepository.findByOwnerId(userid)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        return resume.getData();
    }
@Transactional
    public ResumeDto getResumeByUser(Long userId) {
        return resumeRepository.findByOwnerId(userId)
                .map(this::mapToDto)
                .orElse(null); // or throw a custom exception if preferred
    }
@Transactional
    public String deleteResume(Long userId) {
        resumeRepository.findByOwnerId(userId)
                .ifPresent(resumeRepository::delete);
        return "Success";
    }

    public ResumeDto getResumeById(Long id) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        return mapToDto(resume);
    }

    private ResumeDto mapToDto(Resume resume) {
        ResumeDto dto = new ResumeDto();
        dto.setId(resume.getId());
        dto.setFileName(resume.getFileName());
        dto.setFileType(resume.getFileType());
        dto.setOwnerId(resume.getOwner().getId());
        dto.setOwnerName(resume.getOwner().getName());
        dto.setDateUploaded(resume.getDateUploaded());
        return dto;
    }
}
