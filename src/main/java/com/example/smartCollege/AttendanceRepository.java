package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

	List<Attendance> findByStudent_StudId(Long studId);
    // For Daily/Monthly Reports
    List<Attendance> findByDateBetween(LocalDate start, LocalDate end);
    List<Attendance> findByDate(LocalDate date);
    
    @Query("SELECT a FROM Attendance a LEFT JOIN FETCH a.student")
    List<Attendance> findAllWithStudents();

}
