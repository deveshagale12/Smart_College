package com.example.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Custom method to find a student by email for login
	Optional<Student> findByEmailIgnoreCase(String email);
	Optional<Student> findByEmail(String email);
}