
package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // Optimized for fetching all assignments for Student 16
    List<Assignment> findByStudentStudId(Long studId);
}