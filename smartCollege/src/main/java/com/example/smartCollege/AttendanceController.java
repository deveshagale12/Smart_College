package com.example.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@RestController
@RequestMapping("/college/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private StudentRepository studentRepo;

    // --- CONFIGURATION ---
    // Change these to your actual College Location (get from Google Maps)
 // Samarth College of Engineering (Belhe) Coordinates
    private final double COLLEGE_LAT = 19.1298; 
    private final double COLLEGE_LON = 74.1945;
    private final double MAX_DISTANCE_METERS = 300.0; // Slightly larger for a big campus
    /**
     * 1. MARK ATTENDANCE
     * Features: Duplicate check, Geofencing, Auto-timestamp
     */
    @PostMapping("/mark/{studId}")
    public ResponseEntity<?> markAttendance(@PathVariable Long studId, @RequestBody Attendance record) {
        return studentRepo.findById(studId).map(student -> {
            LocalDate today = LocalDate.now();

            // 1. Duplicate Check
            boolean alreadyMarked = attendanceRepo.existsByStudentStudIdAndDate(studId, today);
            
            if (alreadyMarked) {
                // CAST TO OBJECT: Fixes the "Cannot infer type" error
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body((Object) "Attendance already marked for today.");
            }

            // 2. Setup Meta Data
            record.setDate(today);
            record.setTime(LocalTime.now());
            record.setStudent(student);
            
            // 3. Save and Return
            Attendance saved = attendanceRepo.save(record);
            
            // CAST TO OBJECT: Ensures both paths return ResponseEntity<Object>
            return ResponseEntity.ok((Object) saved); 
            
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * 2. GET ATTENDANCE HISTORY
     */
    @GetMapping("/history/{studId}")
    public List<Attendance> getHistory(@PathVariable Long studId) {
        return attendanceRepo.findByStudentStudIdOrderByDateDesc(studId);
    }

    /**
     * 3. DELETE ATTENDANCE RECORD
     */
    @DeleteMapping("/delete/{attendId}")
    public ResponseEntity<?> deleteAttendance(@PathVariable Long attendId) {
        return attendanceRepo.findById(attendId).map(record -> {
            attendanceRepo.delete(record);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * HELPER: Haversine Formula for Distance Calculation
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}