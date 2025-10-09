package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VacancySearchCriteriaDto {
    private String keyword;
    private String location;
    private String category;
    private Double salaryMin;
    private Double salaryMax;
    private LocalDate postedAfter;
}
