package com.smartCollege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/college/academic")
@CrossOrigin(origins = "*")
public class AcademicController {

    @Autowired
    private AcademicService academicService;

    // GET: Fetch records by Student ID
    // URL: GET http://localhost:8080/college/academic/student/1
    @GetMapping("/student/{studId}")
    public ResponseEntity<List<AcademicRecord>> getStudentHistory(@PathVariable Long studId) {
        return ResponseEntity.ok(academicService.getRecordsByStudent(studId));
    }

    // POST: Insert record for a Student
    // URL: POST http://localhost:8080/college/academic/add/1
    @PostMapping("/add/{studId}")
    public ResponseEntity<?> addAcademicRecord(@PathVariable Long studId, @RequestBody AcademicRecord record) {
        try {
            AcademicRecord savedRecord = academicService.addRecord(studId, record);
            return ResponseEntity.ok(savedRecord);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // PUT: Update record by Record ID
    @PutMapping("/update/{recordId}")
    public ResponseEntity<AcademicRecord> updateYearData(@PathVariable Long recordId, @RequestBody AcademicRecord record) {
        return ResponseEntity.ok(academicService.updateRecord(recordId, record));
    }

    // DELETE: Delete record by Record ID
    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<String> deleteYearData(@PathVariable Long recordId) {
        academicService.deleteRecord(recordId);
        return ResponseEntity.ok("Deleted successfully");
    }
}