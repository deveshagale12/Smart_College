package com.smartCollege;

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
public void markAbsentees() {
    LocalDate today = LocalDate.now();
    studentRepo.findAll().forEach(student -> {
        boolean present = attendanceRepo.existsByStudentStudIdAndDate(student.getStudId(), today);
        if (!present) {
            Attendance absentRecord = new Attendance();
            absentRecord.setDate(today);
            absentRecord.setTime(LocalTime.MIDNIGHT); // Standardize the time
            absentRecord.setStatus("ABSENT");
            absentRecord.setStudent(student);
            absentRecord.setLatitude(0.0); // Explicitly set to zero
            absentRecord.setLongitude(0.0);
            attendanceRepo.save(absentRecord);
        }
    });
}
}
