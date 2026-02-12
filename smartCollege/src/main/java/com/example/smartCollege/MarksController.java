package com.example.smartCollege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/college/students/marks")
@CrossOrigin(origins = "*") // <--- Add this line to ALL Controllers
public class MarksController {

    @Autowired
    private MarksService marksService;

    // Add new marks and automatically calculate grade
    @PostMapping("/add")
    public ResponseEntity<Marks> addMarks(@RequestBody Marks marks) {
        return ResponseEntity.ok(marksService.saveMarks(marks));
    }

    // View marks for a specific student (Shows subjects and grades)
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Marks>> getMarksByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(marksService.getMarksByStudentId(studentId));
    }
}