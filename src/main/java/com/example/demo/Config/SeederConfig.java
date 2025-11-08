package com.example.demo.Config;

import com.example.demo.Models.Application;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.ApplicationRepository;
import com.example.demo.Repository.EducationRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SeederConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public CommandLineRunner seedSupervisorUser() {


        return new CommandLineRunner() {
            @Override
            public void run(String... args) {



                if (!userRepository.findByEmail("Admin12@JobBoard.com").isPresent()) {
                    User newUser = new User();
                    newUser.setUsername("Administrator12");
                    newUser.setEmail("Admin12@JobBoard.com");
                    newUser.setPassword(passwordEncoder.encode("secure@123"));
                    newUser.setRole(Role.ADMIN);

                    userRepository.save(newUser);

                }
            }
        };
    }
}
