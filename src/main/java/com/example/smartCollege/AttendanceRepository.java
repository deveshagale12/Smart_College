package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	boolean existsByStudentStudIdAndDate(Long studId, LocalDate date);
	List<Attendance> findByStudentStudIdOrderByDateDesc(Long studId);
}