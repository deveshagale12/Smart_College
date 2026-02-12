package com.example.smartCollege;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scholarships")
@CrossOrigin(origins = "*") // <--- Add this line to ALL Controllers
public class ScholarshipController {

    @Autowired
    private ScholarshipService scholarshipService;

    // Apply for a scholarship
    // Path variable 'studId' identifies which student is applying
    @PostMapping("/apply/{studId}")
    public ResponseEntity<Scholarship> apply(@PathVariable Long studId, @RequestBody Scholarship scholarship) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scholarshipService.applyForScholarship(studId, scholarship));
    }

    // Approve a scholarship
    @PutMapping("/{scholarshipId}/approve")
    public ResponseEntity<Scholarship> approve(@PathVariable Long scholarshipId) {
        return ResponseEntity.ok(scholarshipService.approveScholarship(scholarshipId));
    }

    // View all scholarships for a specific student
    @GetMapping("/student/{studId}")
    public ResponseEntity<List<Scholarship>> getByStudent(@PathVariable Long studId) {
        return ResponseEntity.ok(scholarshipService.getScholarshipsByStudent(studId));
    }
}