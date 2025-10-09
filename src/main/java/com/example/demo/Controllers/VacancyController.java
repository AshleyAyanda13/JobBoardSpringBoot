package com.example.demo.Controllers;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.EducationDto;
import com.example.demo.DTO.ResponseMessage;
import com.example.demo.DTO.VacancyDto;
import com.example.demo.DTO.VacancySearchCriteriaDto;
import com.example.demo.Models.Vacancy;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Services.VacancyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vacancy")
public class VacancyController {
    @Autowired
    private VacancyService vacancyService;
@Autowired
private UserRepository userRepository;
@Autowired
private JwtUtil jwtUtil;
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")

    @GetMapping("/allPostedVacancies")
    public ResponseEntity<List<Vacancy>> GetAllVacancies(HttpServletRequest request) {

         return ResponseEntity.ok(vacancyService.getAllVacancies());


    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/VacancyDetails")
    public ResponseEntity<VacancyDto> GetVacancyById(HttpServletRequest request,@RequestParam Long Id) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return ResponseEntity.ok(vacancyService.getVacancyById(Id,user.getId()));




    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")

    @GetMapping("/myVacancies")
    public ResponseEntity<List<VacancyDto>> GetMyPostedVacancies(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return ResponseEntity.ok(vacancyService.getVacanciesByUser( user.getId()));


    }
    @PreAuthorize("hasAnyRole('RECRUITER')")
    @GetMapping("/ViewVacancyDetails/{id}")

    public ResponseEntity<VacancyDto> ViewPostedJobDetails(HttpServletRequest request,@PathVariable Long id) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));



        return ResponseEntity.ok(vacancyService.getVacancyById(id,user.getId()));


    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/SearchForVacancy")

    public ResponseEntity<List<Vacancy>> Search(HttpServletRequest request, @RequestBody VacancySearchCriteriaDto vacancySearchCriteriaDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));



        return ResponseEntity.ok(vacancyService.searchVacancies(vacancySearchCriteriaDto));


    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/UploadVacancy")
    public ResponseEntity<VacancyDto> PostVacancy(HttpServletRequest request,@RequestBody VacancyDto vacancyDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return ResponseEntity.ok(vacancyService.createVacancy(user.getId(),vacancyDto));


    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PutMapping("/UpdateVacancy")
    public ResponseEntity<VacancyDto> GetMyPostedVacancies(HttpServletRequest request, @PathVariable Long Id ,@RequestBody VacancyDto vacancyDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return ResponseEntity.ok(vacancyService.updateVacancy(user.getId(),Id,vacancyDto));


    }
    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @DeleteMapping("/DeleteAVacancy")
    public ResponseEntity<String> DeleteMyPostedVacancy(HttpServletRequest request, @PathVariable Long Id ,@RequestBody VacancyDto vacancyDto) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtUtil.extractUsername(token);
        com.example.demo.Models.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        vacancyService.deleteVacancy(user.getId(),Id);
        ResponseMessage responseMessage=new ResponseMessage();
        responseMessage.Message="Deletion Successful";
        return ResponseEntity.ok(responseMessage.toString());




    }






}
