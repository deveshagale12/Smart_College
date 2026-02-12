package com.example.smartCollege;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parents")
@CrossOrigin(origins = "*") // <--- Add this line to ALL Controllers
public class ParentController {

    @Autowired
    private ParentService parentService;

    // 1. View Student Progress (Marks & Attendance)
    @GetMapping("/{parentId}/progress")
    public ResponseEntity<Map<String, Object>> viewStudentProgress(@PathVariable Long parentId) {
        return ResponseEntity.ok(parentService.getChildProgress(parentId));
    }

    // 2. View Fees Status
    @GetMapping("/{parentId}/fees")
    public ResponseEntity<Fees> viewFeesStatus(@PathVariable Long parentId) {
        return ResponseEntity.ok(parentService.getChildFees(parentId));
    }

    // 3. Communicate with Faculty (Simple Message Simulation)
    @PostMapping("/{parentId}/communicate")
    public ResponseEntity<String> communicateFaculty(
            @PathVariable Long parentId, 
            @RequestParam Long facultyId, 
            @RequestBody String message) {
        return ResponseEntity.ok("Message sent from Parent ID " + parentId + " to Faculty ID " + facultyId);
    }
}