package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long>{

	List<Attendance> findByStudent_StudId(Long studId);
    // For Daily/Monthly Reports
    List<Attendance> findByDateBetween(LocalDate start, LocalDate end);
    List<Attendance> findByDate(LocalDate date);
	// This fixes the "findByStudentStudIdOrderByDateDesc" error
    List<Attendance> findByStudentStudIdOrderByDateDesc(Long studId);

	boolean existsByStudentStudIdAndDate(Long studId, LocalDate date);
    
    @Query("SELECT a FROM Attendance a LEFT JOIN FETCH a.student")
    List<Attendance> findAllWithStudents();

}
