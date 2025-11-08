package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {

    private String username;
    private String title;
    private String name;
    private String surname;

    private String email;

    private String password;
    private String repeatPassword;

    private Date DateOfBirth;
    @JsonProperty("IDNumber")
    private String idNumber;
    private String Gender;

    private String company;

    private String phoneNumber;
}
