
package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
// If you are also using HashMap, add this too:
import java.util.HashMap;
@RestController
@RequestMapping("/api/assignments")
@CrossOrigin("*")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepo;

    // 1. POST: Create/Submit Assignment for a specific Student (e.g., 16)
    @PostMapping("/submit/{studId}")
    public ResponseEntity<Assignment> submit(@PathVariable Long studId, @RequestBody Assignment assignment) {
        Student student = new Student();
        student.setStudId(studId);
        assignment.setStudent(student);
        assignment.setSubmissionDate(LocalDateTime.now());
        assignment.setStatus("Submitted");
        return ResponseEntity.ok(assignmentRepo.save(assignment));
    }

    // 2. GET ALL: List every assignment in NeonDB
    @GetMapping("/all")
    public List<Assignment> getAll() {
        return assignmentRepo.findAll();
    }

    // 3. GET BY STUDENT: Fast process for Student 16
    @GetMapping("/student/{studId}")
    public List<Assignment> getByStudent(@PathVariable Long studId) {
        return assignmentRepo.findByStudentStudId(studId);
    }

    // 4. GET BY ID: Single assignment details
    @GetMapping("/{id}")
    public ResponseEntity<Assignment> getById(@PathVariable Long id) {
        return assignmentRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. UPDATE: Edit assignment details or status
 // 5. UPDATE: Safe grading that preserves student work
    @PutMapping("/update/{id}")
    public ResponseEntity<Assignment> update(@PathVariable Long id, @RequestBody Assignment details) {
        return assignmentRepo.findById(id).map(existing -> {
            
            // --- FACULTY GRADING FIELDS ---
            // Only update these if they are present in the request
            if (details.getStatus() != null) existing.setStatus(details.getStatus());
            if (details.getFeedback() != null) existing.setFeedback(details.getFeedback());
            if (details.getMarks() != null) existing.setMarks(details.getMarks());
            
            // --- STUDENT CONTENT PROTECTION ---
            // Only update these IF the request actually contains them. 
            // This prevents the "null" issue when a teacher just submits a grade.
            if (details.getTitle() != null && !details.getTitle().isEmpty()) {
                existing.setTitle(details.getTitle());
            }
            if (details.getDescription() != null && !details.getDescription().isEmpty()) {
                existing.setDescription(details.getDescription());
            }
            if (details.getContent() != null && !details.getContent().isEmpty()) {
                existing.setContent(details.getContent());
            }
            
            return ResponseEntity.ok(assignmentRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
    // 6. DELETE: Remove from NeonDB
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assignmentRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/test-minimal")
    public List<Object[]> getMinimal() {
        // This fetches only IDs and Titles, bypassing the PDF and Student objects
        return assignmentRepo.findAll().stream()
            .map(a -> new Object[]{a.getId(), a.getTitle()})
            .collect(Collectors.toList());
    }
}