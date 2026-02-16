package com.example.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AcademicRepository extends JpaRepository<AcademicRecord, Long> {
    List<AcademicRecord> findByStudentStudId(Long studId);
}