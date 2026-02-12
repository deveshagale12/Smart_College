package com.example.smartCollege;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    @Autowired
    
    private AttendanceRepository attendanceRepository;

    public Attendance saveAttendance(Attendance attendance) {
        // Auto-fill date and time if they are null
        if (attendance.getDate() == null) attendance.setDate(LocalDate.now());
        if (attendance.getTime() == null) attendance.setTime(LocalTime.now());
        
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getStudentAttendance(Long studId) {
        return attendanceRepository.findByStudent_StudId(studId);
    }
    public Double calculateAttendancePercentage(Long studId) {
        List<Attendance> records = attendanceRepository.findByStudent_StudId(studId);
        
        if (records.isEmpty()) {
            return 0.0;
        }

        long presentCount = records.stream()
                .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus()))
                .count();

        // Math: (Present / Total) * 100
        return (double) (presentCount * 100) / records.size();
    }
}