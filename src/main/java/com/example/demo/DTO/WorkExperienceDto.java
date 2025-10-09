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
public class WorkExperienceDto {
    @NotBlank(message = "Company is Required")
    private String company;
    @NotBlank(message = "Position is required")
    private String position;
    @NotBlank(message = "Start Date is Required")
    private LocalDate startDate;
    @NotBlank(message = "End Date is required")
    private LocalDate endDate;

    private String description;

    @ManyToOne
    private com.example.demo.Models.User user;

}
