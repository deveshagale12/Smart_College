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

    // Runs every night at 11:59 PM
    @Scheduled(cron = "0 59 23 * * *")
    public void markAbsentees() {
        LocalDate today = LocalDate.now();
        studentRepo.findAll().forEach(student -> {
            boolean present = attendanceRepo.existsByStudentStudIdAndDate(student.getStudId(), today);
            if (!present) {
                Attendance absentRecord = new Attendance();
                absentRecord.setDate(today);
                absentRecord.setStatus("ABSENT");
                absentRecord.setStudent(student);
                attendanceRepo.save(absentRecord);
            }
        });
    }
}