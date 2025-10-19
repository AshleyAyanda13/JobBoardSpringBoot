package com.example.demo.Models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Resume {
    @Id
    @GeneratedValue
    private Long id;
    private String fileName;
    private String fileType;
    private LocalDate dateUploaded;
    @Lob
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "owner_id", unique = true)
    private com.example.demo.Models.User owner;
}