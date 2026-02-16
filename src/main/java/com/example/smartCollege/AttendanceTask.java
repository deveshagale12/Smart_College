package com.example.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

import java.time.LocalDate;
@Component
public class AttendanceTask {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private AttendanceRepository attendanceRepo;

    // Cron expression: Seconds, Minutes, Hours, Day, Month, Day of Week
    // "0 59 23 * * *" means: Run at 23:59:00 every single day
    @Scheduled(cron = "0 59 23 * * *")
    public void markAbsentForMissingStudents() {
        LocalDate today = LocalDate.now();
        
        // 1. Get all students
        studentRepo.findAll().forEach(student -> {
            
            // 2. Check if this specific student marked attendance today
            boolean present = attendanceRepo.existsByStudentStudIdAndDate(student.getStudId(), today);
            
            // 3. If no record found, they are absent
            if (!present) {
                Attendance absentRecord = new Attendance();
                absentRecord.setDate(today);
                absentRecord.setTime(LocalTime.MIDNIGHT); // Standard time for auto-records
                absentRecord.setStatus("ABSENT");
                absentRecord.setStudent(student);
                absentRecord.setLatitude(0.0); // No GPS for absent records
                absentRecord.setLongitude(0.0);
                
                attendanceRepo.save(absentRecord);
            }
        });
        
        System.out.println("Nightly attendance sweep completed for: " + today);
    }
}