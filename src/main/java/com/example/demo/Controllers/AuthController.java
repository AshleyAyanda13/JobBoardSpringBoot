package com.example.demo.Controllers;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.JwtResponse;
import com.example.demo.DTO.RegistrationDto;
import com.example.demo.DTO.ResponseMessage;
import com.example.demo.DTO.UserDto;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
@Autowired
    private PasswordEncoder passwordEncoder;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
       private JwtUtil jwtUtil;

        @Autowired
        private UserDetailsService userDetailsService;
@Autowired
private UserRepository userRepository;
        @PostMapping("/authenticate")
        public ResponseEntity<?> createAuthToken(@RequestBody UserDto authRequest) throws Exception {

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
                );
            } catch (Exception e) {
                System.out.println("Authentication failed: " + e.getMessage());
                return ResponseEntity.status(403).body("Invalid credentials");
            }

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            User existingUser = userRepository.findByUsername("wow").get();

            final String jwt = jwtUtil.generateToken(userDetails, user.getId(),user.getRole().name());


            return ResponseEntity.ok(new JwtResponse(jwt));
        }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
       if (userRepository.findByEmail(user.getEmail()).isPresent()) {
          return ResponseEntity.badRequest().body("User already exists.");
        }


        if (!user.getPassword().equals(user.getRepeatPassword())) {


            return ResponseEntity.badRequest().body("Password and Confirm Password don't match");
        }


        User userr=new User();
        userr.setEmail(user.getEmail());
        userr.setPassword(passwordEncoder.encode(user.getPassword()));
        userr.setRole(Role.JOBSEEKER);
        userr.setUsername(user.getUsername());
        userr.setName(user.getName());
        userr.setPhoneNumber(user.getPhoneNumber());
        userr.setSurname(user.getSurname());
        userr.setIdNumber(user.getIdNumber());




        userRepository.save(userr);
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.Message="Registration Successful";
        return ResponseEntity.ok(responseMessage);

    }





    @PostMapping("/registerrecruiter")
    public ResponseEntity<?> registerrRecruiter(@RequestBody RegistrationDto user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists.");
        }




        if (!user.getPassword().equals(user.getRepeatPassword())) {


            return ResponseEntity.badRequest().body("Password and Confirm Password don't match");
        }


        User userr=new User();
        userr.setEmail(user.getEmail());
        userr.setPassword(passwordEncoder.encode(user.getPassword()));
        userr.setRole(Role.RECRUITER);
        userr.setUsername(user.getUsername());

        userRepository.save(userr);
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.Message="Registration Successful";
        return ResponseEntity.ok(responseMessage);

    }

}













