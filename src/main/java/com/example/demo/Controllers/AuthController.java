package com.example.demo.Controllers;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.JwtResponse;
import com.example.demo.DTO.RegistrationDto;
import com.example.demo.DTO.ResponseMessage;
import com.example.demo.DTO.UserDto;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Services.EmailService;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
@Autowired
    private PasswordEncoder passwordEncoder;

        @Autowired
        private AuthenticationManager authenticationManager;
@Autowired
private EmailService emailService;
        @Autowired
       private JwtUtil jwtUtil;

        @Autowired
        private UserDetailsService userDetailsService;
@Autowired
private UserRepository userRepository;
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody UserDto authRequest, HttpServletResponse response) {
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

        final String jwt = jwtUtil.generateToken(userDetails, user.getId(), user.getRole().name());


        ResponseCookie cookie = ResponseCookie.from("authToken", jwt)
                .httpOnly(true)
                .secure(false) // ❗ Use false for localhost (true only for HTTPS)
                .path("/")
                .maxAge(3600)
                .sameSite("Lax") // ❗ Use "Lax" for localhost; "Strict" blocks cross-origin cookies
                .build();



        response.setHeader("Set-Cookie", cookie.toString());

        Map<String, Object> userInfo = Map.of(
                "username", user.getUsername(),
                "role", user.getRole().name()
        );

        return ResponseEntity.ok(userInfo);
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
//        emailService.sendSimpleEmail(
//                user.getEmail(),
//                "Welcome to JobBoard",
//                "Hi " + user.getName() + ", thanks for registering!"
//        );

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

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String jwt = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if (jwt == null || !jwt.contains(".")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No valid token");
        }

        String username = jwtUtil.extractUsername(jwt);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole().name()
        ));
    }

}













