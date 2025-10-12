package  com.example.demo.Repository;

import com.example.demo.DTO.WorkExperienceDto;
import  com.example.demo.Models.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience, Long> {


    List<WorkExperience> findByUser_Id(Long userId);
    WorkExperience findByUser_IdAndId(Long userId,Long Id);
    void deleteByUser_Id(Long userId);
     boolean existsByUser_Id(Long UserId);
}
