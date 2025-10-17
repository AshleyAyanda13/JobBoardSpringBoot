package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor

@RequiredArgsConstructor
public class UserInfoDto {



    private String username;
    private String name;
    private String surname;
    private String jobTitle;
}
