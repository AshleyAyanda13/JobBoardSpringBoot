package  com.example.demo.Repository;

import  com.example.demo.Models.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {


    Optional<WorkExperience> findByUser_Id(Long userId);
    void deleteByUser_Id(Long userId);
     boolean existsByUser_Id(Long UserId);
}
