package com.example.demo.DTO;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationDto {
    @NotBlank(message = "Id Required")
    private Long id;
    @NotBlank(message = "Institution is required")
    private String institution;
    @NotBlank(message = "Qualification is required")
    private String degree;

    private LocalDate startDate;
    private LocalDate endDate;
    private String fieldOfStudy;

    @ManyToOne
    private com.example.demo.Models.User user;

}
