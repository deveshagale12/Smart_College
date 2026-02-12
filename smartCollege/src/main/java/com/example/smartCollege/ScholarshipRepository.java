package com.example.smartCollege;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
    // Navigates through the 'student' field in Scholarship to find the 'studId'
    List<Scholarship> findByStudent_StudId(Long studId);
}