package com.example.demo.DTO;

import com.example.demo.Models.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDto {



    @NotBlank(message = "Job Title is required")
    private String jobTitle;

    private  String jobDescription;
    private LocalDate datePosted;
    private LocalDate EndDate;
    private String location;
    private String category;
    private Double salary;
    private LocalDate postedDate;
    @ManyToOne
    private User employer;
}
