package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResumeDto {
    private Long id;
    private String fileName;
    private String fileType;
    private Long ownerId;
    private String ownerName;
    private LocalDate dateUploaded;
}
