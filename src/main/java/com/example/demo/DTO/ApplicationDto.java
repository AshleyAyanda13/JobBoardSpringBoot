package com.example.demo.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationDto {

    private LocalDate appliedDate;
    private Long applicantId;
    private String applicantName;
    private Long vacancyId;
    private String jobTitle;
    private Long resumeId;
    private String coverletter;
    // Applicant info

    private String applicantSurname;
    private String applicantEmail;
    private String applicantPhone;

}
