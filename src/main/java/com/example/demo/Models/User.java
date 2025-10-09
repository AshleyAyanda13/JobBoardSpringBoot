package com.example.demo.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    private String surname;
    private String name;
    private  String email;
    private String password;
    private String phoneNumber;
    private String idNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
}
