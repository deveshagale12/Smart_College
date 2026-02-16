package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {
    
    // Find fees for a specific student
    Optional<Fees> findByStudentStudId(Long studId);

    // Get all records where the student still owes money
    @Query("SELECT f FROM Fees f WHERE f.dueAmount > 0")
    List<Fees> findAllOverdueFees();
}