package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/college/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepo;

    @Autowired
    private StudentRepository studentRepo;

    // --- CONFIGURATION ---
    private final double COLLEGE_LAT = 19.1298; 
    private final double COLLEGE_LON = 74.1945;
    private final double MAX_DISTANCE_METERS = 300.0;
    
    // Define your college start time
    private final LocalTime LATE_THRESHOLD = LocalTime.of(9, 15);

    /**
     * 1. MARK ATTENDANCE (With Geofencing & Late check)
     */
    @PostMapping("/mark/{studId}")
    public ResponseEntity<?> markAttendance(@PathVariable Long studId, @RequestBody Attendance record) {
        return studentRepo.findById(studId).map(student -> {
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();

            // A. Geofencing Verification
            double distance = calculateDistance(record.getLatitude(), record.getLongitude(), COLLEGE_LAT, COLLEGE_LON);
            if (distance > MAX_DISTANCE_METERS) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Out of range. You must be on campus to mark attendance.");
            }

            // B. Duplicate Check
            if (attendanceRepo.existsByStudentStudIdAndDate(studId, today)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Attendance already recorded for today.");
            }

            // C. Late Status Logic
            if (now.isAfter(LATE_THRESHOLD)) {
                record.setStatus("LATE");
            } else {
                record.setStatus("PRESENT");
            }

            record.setDate(today);
            record.setTime(now);
            record.setStudent(student);
            
            return ResponseEntity.ok(attendanceRepo.save(record)); 
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * 2. AUTO-MARK ABSENT
     * This endpoint finds all students who HAVEN'T marked attendance today 
     * and creates an "ABSENT" record for them.
     */
    @PostMapping("/process-absentees")
    public ResponseEntity<?> processAbsentees() {
        LocalDate today = LocalDate.now();
        List<Student> allStudents = studentRepo.findAll();
        
        int count = 0;
        for (Student student : allStudents) {
            // If no record (Present/Late/Absent) exists for this student today
            if (!attendanceRepo.existsByStudentStudIdAndDate(student.getStudId(), today)) {
                Attendance absentRecord = new Attendance();
                absentRecord.setStudent(student);
                absentRecord.setDate(today);
                absentRecord.setTime(LocalTime.now());
                absentRecord.setStatus("ABSENT");
                absentRecord.setLatitude(0.0); // No location for absentees
                absentRecord.setLongitude(0.0);
                attendanceRepo.save(absentRecord);
                count++;
            }
        }
        return ResponseEntity.ok("Processed " + count + " students as ABSENT for " + today);
    }

    /**
     * 3. GET ATTENDANCE HISTORY
     */
    @GetMapping("/history/{studId}")
    public List<Attendance> getHistory(@PathVariable Long studId) {
        return attendanceRepo.findByStudentStudIdOrderByDateDesc(studId);
    }

    /**
     * 4. ALL ATTENDANCE (Filter by date)
     */
    @GetMapping("/all")
    public ResponseEntity<List<Attendance>> getAllAttendance(@RequestParam(required = false) String date) {
        if (date != null) {
            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(attendanceRepo.findByDate(localDate));
        }
        return ResponseEntity.ok(attendanceRepo.findAll());
    }

    /**
     * 5. MONTHLY REPORT
     */
    @GetMapping("/report")
    public ResponseEntity<List<Attendance>> getMonthlyReport(
            @RequestParam int month, 
            @RequestParam int year) {
        
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        
        return ResponseEntity.ok(attendanceRepo.findByDateBetween(start, end));
    }

    /**
     * HELPER: Haversine Formula
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
