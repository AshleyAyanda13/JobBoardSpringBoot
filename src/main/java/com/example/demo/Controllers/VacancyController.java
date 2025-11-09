package com.example.demo.Controllers;

import com.example.demo.Config.JwtCookieUtil;
import com.example.demo.DTO.ResponseMessage;
import com.example.demo.DTO.VacancyDto;
import com.example.demo.DTO.VacancySearchCriteriaDto;
import com.example.demo.Models.User;
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
    private JwtCookieUtil jwtCookieUtil;

    private User getAuthenticatedUser(HttpServletRequest request) {
        String username = jwtCookieUtil.extractUsernameFromCookie(request);
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @GetMapping("/allPostedVacancies")
    public ResponseEntity<List<Vacancy>> GetAllVacancies() {
        return ResponseEntity.ok(vacancyService.getAllVacancies());
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER','RECRUITER', 'ADMIN')")
    @GetMapping("/VacancyDetails/{Id}")
    public ResponseEntity<VacancyDto> GetVacancyById(HttpServletRequest request, @PathVariable Long Id) {
        User user = getAuthenticatedUser(request);
        return ResponseEntity.ok(vacancyService.getVacancyById(Id, user.getId()));
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @GetMapping("/myVacancies")
    public ResponseEntity<List<VacancyDto>> GetMyPostedVacancies(HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        return ResponseEntity.ok(vacancyService.getVacanciesByUser(user.getId()));
    }

    @PreAuthorize("hasAnyRole('RECRUITER')")
    @GetMapping("/ViewVacancyDetails/{id}")
    public ResponseEntity<VacancyDto> ViewPostedJobDetails(HttpServletRequest request, @PathVariable Long id) {
        User user = getAuthenticatedUser(request);
        return ResponseEntity.ok(vacancyService.getVacancyById(id, user.getId()));
    }

    @PreAuthorize("hasAnyRole('JOBSEEKER', 'ADMIN')")
    @PostMapping("/SearchForVacancy")
    public ResponseEntity<List<VacancyDto>> Search(HttpServletRequest request, @RequestBody VacancySearchCriteriaDto criteria) {
        User user = getAuthenticatedUser(request);
        System.out.println("Search request by: " + user.getUsername());
        System.out.println("Keyword: " + criteria.getKeyword());
        System.out.println("Location: " + criteria.getLocation());
        return ResponseEntity.ok(vacancyService.searchVacancies(criteria));
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @PostMapping("/UploadVacancy")
    public ResponseEntity<VacancyDto> PostVacancy(HttpServletRequest request, @RequestBody VacancyDto vacancyDto) {
        User user = getAuthenticatedUser(request);
        return ResponseEntity.ok(vacancyService.createVacancy(user.getId(), vacancyDto));
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @PutMapping("/UpdateVacancy/{Id}")
    public ResponseEntity<VacancyDto> UpdateMyPostedVacancy(HttpServletRequest request, @PathVariable Long Id, @RequestBody VacancyDto vacancyDto) {
        User user = getAuthenticatedUser(request);
        return ResponseEntity.ok(vacancyService.updateVacancy(user.getId(), Id, vacancyDto));
    }

    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    @DeleteMapping("/DeleteAVacancy/{Id}")
    public ResponseEntity<String> DeleteMyPostedVacancy(HttpServletRequest request, @PathVariable Long Id) {
        User user = getAuthenticatedUser(request);
        vacancyService.deleteVacancy(user.getId(), Id);
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.Message = "Deletion Successful";
        return ResponseEntity.ok(responseMessage.toString());
    }
}