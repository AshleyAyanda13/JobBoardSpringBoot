package com.example.demo.Repository;

import com.example.demo.Models.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByOwnerId(Long ownerId);

}
