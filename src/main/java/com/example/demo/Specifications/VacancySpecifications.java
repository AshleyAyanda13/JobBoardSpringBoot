package com.example.demo.Specifications;

import com.example.demo.Models.Vacancy;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class VacancySpecifications {
    public static Specification<Vacancy> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("jobTitle")), likePattern),
                    cb.like(cb.lower(root.get("jobDescription")), likePattern)
            );
        };
    }

    public static Specification<Vacancy> hasLocation(String location) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("location")), location.toLowerCase());
    }

    public static Specification<Vacancy> hasCategory(String category) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Vacancy> hasSalaryBetween(Double min, Double max) {
        return (root, query, cb) -> cb.between(root.get("salary"), min, max);
    }

    public static Specification<Vacancy> postedAfter(LocalDate date) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("datePosted"), date);
    }
}

