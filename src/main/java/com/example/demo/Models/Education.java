package com.example.demo.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Education {

    @Id
    @GeneratedValue
    private Long id;
    private String institution;
    private String degree;
    private LocalDate startDate;
    private LocalDate endDate;
    private String fieldOfStudy;

    @ManyToOne
    private com.example.demo.Models.User user;





}
