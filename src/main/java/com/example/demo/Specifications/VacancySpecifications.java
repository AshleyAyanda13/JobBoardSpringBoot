package com.example.demo.Specifications;

import com.example.demo.Models.Vacancy;
import org.springframework.data.jpa.domain.Specification;

public class VacancySpecifications {

    public static Specification<Vacancy> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("jobTitle")), likePattern),
                    cb.like(cb.lower(root.get("jobDescription")), likePattern),
                    cb.like(cb.lower(root.join("employer").get("name")), likePattern)
            );
        };
    }

    public static Specification<Vacancy> hasLocation(String location) {
        String likePattern = "%" + location.toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("location")), likePattern);
    }
}