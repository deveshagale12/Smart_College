package com.example.smartCollege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*") // <--- Add this line to ALL Controllers
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // mark attendance
    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
        return ResponseEntity.ok(attendanceService.saveAttendance(attendance));
    }

    // view attendance for a specific student
    @GetMapping("/student/{studId}")
    public ResponseEntity<List<Attendance>> viewAttendance(@PathVariable Long studId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendance(studId));
    }
    
 // Get attendance percentage for a student
    @GetMapping("/student/{studId}/percentage")
    public ResponseEntity<String> getPercentage(@PathVariable Long studId) {
        Double percentage = attendanceService.calculateAttendancePercentage(studId);
        return ResponseEntity.ok("Attendance for Student ID " + studId + " is: " + percentage + "%");
    }
}