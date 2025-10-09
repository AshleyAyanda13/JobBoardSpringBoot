package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkExperience
{
    @Id
    @GeneratedValue
    private Long id;
    private String company;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)

    private com.example.demo.Models.User user;




}
