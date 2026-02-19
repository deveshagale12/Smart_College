package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

import java.time.LocalDate;
@Component
public class AttendanceScheduler {

    @Autowired
    private StudentRepository studentRepo;
    
    @Autowired
    private AttendanceRepository attendanceRepo;

    /**
     * Runs at 4:01 PM every day (16:01:00)
     * Cron format: "sec min hour day month day-of-week"
     */
    @Scheduled(cron = "0 1 16 * * *")
    public void markAbsentees() {
        LocalDate today = LocalDate.now();
        
        studentRepo.findAll().forEach(student -> {
            // If no record exists (neither PRESENT nor LATE), mark them ABSENT
            boolean recorded = attendanceRepo.existsByStudentStudIdAndDate(student.getStudId(), today);
            
            if (!recorded) {
                Attendance absentRecord = new Attendance();
                absentRecord.setDate(today);
                absentRecord.setTime(LocalTime.of(16, 0)); // Marked exactly at cutoff
                absentRecord.setStatus("ABSENT");
                absentRecord.setStudent(student);
                absentRecord.setLatitude(0.0);
                absentRecord.setLongitude(0.0);
                attendanceRepo.save(absentRecord);
            }
        });
    }
}