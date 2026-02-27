package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
// If you are also using HashMap, add this too:
import java.util.HashMap;

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
	/*
	 * private final double COLLEGE_LAT = 19.1298; private final double COLLEGE_LON
	 * = 74.1945; private final double MAX_DISTANCE_METERS = 300.0; // Slightly
	 * larger for a big campus
	 */    /**
     * 1. MARK ATTENDANCE
     * Features: Duplicate check, Geofencing, Auto-timestamp
     */
    
 // --- TEMPORARY TESTING CONFIGURATION ---
 // Replace these with your current home/office coordinates
 private final double COLLEGE_LAT = 19.129892; // Your current Lat
 private final double COLLEGE_LON = 74.190626; // Your current Lon
 private final double MAX_DISTANCE_METERS = 5000.0; // Keep it tight for testing
 
 @PostMapping("/mark/{studId}")
public ResponseEntity<?> markAttendance(@PathVariable Long studId, @RequestBody Attendance record) {
    try {
        return studentRepo.findById(studId).map(student -> {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            // 1. Safe Geofencing Verification
            if (record.getLatitude() != null && record.getLongitude() != null) {
                double distance = calculateDistance(record.getLatitude(), record.getLongitude(), COLLEGE_LAT, COLLEGE_LON);
                if (distance > MAX_DISTANCE_METERS) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                         .body("Out of range. You must be on campus. Distance: " + Math.round(distance) + "m");
                }
            }

            // 2. Duplicate Check
            if (attendanceRepo.existsByStudentStudIdAndDate(studId, today)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Attendance already marked for today.");
            }

            // 3. Updated Status Logic for Testing
            if (record.getStatus() == null || record.getStatus().isEmpty()) {
                // --- FOR TESTING: Changed start time to midnight ---
                LocalTime startPresent = LocalTime.of(0, 0); 
                LocalTime startLate = LocalTime.of(10, 30);
                LocalTime endDay = LocalTime.of(16, 0);

                if (now.isBefore(startPresent)) {
                    // This block is unlikely to be hit if startPresent is 00:00
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                         .body("Attendance hasn't started yet.");
                } else if (now.isBefore(startLate)) {
                    record.setStatus("PRESENT");
                } else if (now.isBefore(endDay)) {
                    record.setStatus("LATE");
                } else {
                    record.setStatus("ABSENT");
                }
            }

            record.setDate(today);
            record.setTime(now);
            record.setStudent(student);
            
            return ResponseEntity.ok(attendanceRepo.save(record)); 
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    }
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
    @GetMapping("/all")
    public ResponseEntity<List<Attendance>> getAllAttendance(@RequestParam(required = false) String date) {
        if (date != null) {
            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(attendanceRepo.findByDate(localDate));
        }
        return ResponseEntity.ok(attendanceRepo.findAll());
    }
    @GetMapping("/report")
    public ResponseEntity<List<Attendance>> getMonthlyReport(
            @RequestParam int month, 
            @RequestParam int year) {
        
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        
        return ResponseEntity.ok(attendanceRepo.findByDateBetween(start, end));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return attendanceRepo.findById(id).map(record -> {
            record.setStatus(status.toUpperCase());
            attendanceRepo.save(record);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/stats/monthly")
    public ResponseEntity<Map<Long, Double>> getMonthlyPercentages(@RequestParam int month, @RequestParam int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        
        // Get all records for the month
        List<Attendance> records = attendanceRepo.findByDateBetween(start, end);
        
        // Group by student ID and calculate percentage
        // (Count of PRESENT / Total Records for that student) * 100
        Map<Long, Double> stats = records.stream()
            .collect(Collectors.groupingBy(
                att -> att.getStudent().getStudId(),
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> {
                        long presentCount = list.stream()
                            .filter(a -> "PRESENT".equals(a.getStatus()))
                            .count();
                        return (double) Math.round(((double) presentCount / list.size()) * 100);
                    }
                )
            ));
        return ResponseEntity.ok(stats);
    }
    
}