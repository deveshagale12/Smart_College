package com.SmartCollege;
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

    // POST: Save a year's data for a student
    @PostMapping("/{studId}")
    public ResponseEntity<AcademicRecord> addYearData(@PathVariable Long studId, @RequestBody AcademicRecord record) {
        return ResponseEntity.ok(academicService.addRecord(studId, record));
    }

    // GET: Get all yearly records for a student
    @GetMapping("/{studId}")
    public ResponseEntity<List<AcademicRecord>> getStudentHistory(@PathVariable Long studId) {
        return ResponseEntity.ok(academicService.getRecordsByStudent(studId));
    }
    
 // PUT: Update an existing record by its own ID
    @PutMapping("/{recordId}")
    public ResponseEntity<AcademicRecord> updateYearData(@PathVariable Long recordId, @RequestBody AcademicRecord record) {
        return ResponseEntity.ok(academicService.updateRecord(recordId, record));
    }

    // DELETE: Delete a record by its own ID
    @DeleteMapping("/{recordId}")
    public ResponseEntity<String> deleteYearData(@PathVariable Long recordId) {
        academicService.deleteRecord(recordId);
        return ResponseEntity.ok("Academic record deleted successfully");
    }

}
