package com.example.demo.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String jobTitle;
    private  String jobDescription;
    private LocalDate datePosted;
    private LocalDate endDate;
    private String location;
    private String category;
    private Double salary;



    private LocalDate lastUpdatedDate;
    @ManyToOne
    private User employer;
}
