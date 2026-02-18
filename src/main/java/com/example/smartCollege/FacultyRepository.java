package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByEmail(String email);
 // This is the fix: It fetches the faculty AND their students in one go
    @Query("SELECT DISTINCT f FROM Faculty f LEFT JOIN FETCH f.students")
    List<Faculty> findAllWithStudents();
}