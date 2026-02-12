package com.example.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*") // <--- Add this line to ALL Controllers
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    // 1. Register
    @PostMapping("/register")
    public ResponseEntity<Faculty> register(@RequestBody Faculty faculty) {
        return ResponseEntity.ok(facultyService.registerFaculty(faculty));
    }

    // 2. Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        boolean isAuthenticated = facultyService.login(email, password);
        return isAuthenticated ? ResponseEntity.ok("Login Successful") : 
                                 ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
    }

    // 3. Take Attendance (Reuses your Attendance Logic)
    @PostMapping("/attendance/mark")
    public ResponseEntity<String> takeAttendance(@RequestBody Attendance attendance) {
        facultyService.markAttendance(attendance);
        return ResponseEntity.ok("Attendance Marked successfully");
    }

    // 4. Assign Marks (Reuses your Marks Logic)
    @PostMapping("/marks/assign")
    public ResponseEntity<String> assignMarks(@RequestBody Marks marks) {
        facultyService.assignMarks(marks);
        return ResponseEntity.ok("Marks Assigned successfully");
    }

    // 5. Upload Notes (Simulated via String URL/Path)
    @PostMapping("/{facultyId}/notes/upload")
    public ResponseEntity<String> uploadNotes(@PathVariable Long facultyId, @RequestParam String noteTitle) {
        return ResponseEntity.ok("Notes '" + noteTitle + "' uploaded by Faculty ID: " + facultyId);
    }

    // 6. Apply Leave
    @PostMapping("/{facultyId}/leave")
    public ResponseEntity<String> applyLeave(@PathVariable Long facultyId, @RequestParam String reason) {
        return ResponseEntity.ok("Leave application submitted for Faculty ID: " + facultyId);
    }
}