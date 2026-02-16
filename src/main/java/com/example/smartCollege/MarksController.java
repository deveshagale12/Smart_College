package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/college/marks")
@CrossOrigin(origins = "*")
public class MarksController {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 1. ADD Marks for a Student
    @PostMapping("/{studId}")
    public ResponseEntity<Marks> addMarks(@PathVariable Long studId, @RequestBody Marks marks) {
        return studentRepository.findById(studId).map(student -> {
            marks.setStudent(student);
            return ResponseEntity.ok(marksRepository.save(marks));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 2. GET all marks for a specific student
    @GetMapping("/student/{studId}")
    public List<Marks> getMarksByStudent(@PathVariable Long studId) {
        return marksRepository.findByStudentStudId(studId);
    }

    // 3. DELETE Marks
    @DeleteMapping("/{marksId}")
    public ResponseEntity<?> deleteMarks(@PathVariable Long marksId) {
        return marksRepository.findById(marksId).map(m -> {
            marksRepository.delete(m);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{marksId}")
    public ResponseEntity<Marks> updateMarks(@PathVariable Long marksId, @RequestBody Marks marksDetails) {
        return marksRepository.findById(marksId).map(existingMarks -> {
            // Update fields
            existingMarks.setSubject(marksDetails.getSubject());
            existingMarks.setTestType(marksDetails.getTestType());
            existingMarks.setOutOf(marksDetails.getOutOf());
            existingMarks.setObtainedMarks(marksDetails.getObtainedMarks());
            
            Marks updatedMarks = marksRepository.save(existingMarks);
            return ResponseEntity.ok(updatedMarks);
        }).orElse(ResponseEntity.notFound().build());
    }
}