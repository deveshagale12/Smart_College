package com.example.smartCollege;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Custom query to find students by course
    List<Student> findByCourse(String course);
    Optional<Student> findByEmail(String email);
}