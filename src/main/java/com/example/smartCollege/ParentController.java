package com.example.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/college/parents")
@CrossOrigin(origins = "*")
public class ParentController {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentRepository studentRepository;

    // 1. ADD Parent to a Student
    @PostMapping("/{studId}")
    public ResponseEntity<Parent> addParent(@PathVariable Long studId, @RequestBody Parent parent) {
        return studentRepository.findById(studId).map(student -> {
            parent.setStudent(student);
            return ResponseEntity.ok(parentRepository.save(parent));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 2. GET all parents for a specific student
    @GetMapping("/student/{studId}")
    public List<Parent> getParentsByStudent(@PathVariable Long studId) {
        return parentRepository.findByStudentStudId(studId);
    }

    // 3. UPDATE Parent Details
    @PutMapping("/{parentId}")
    public ResponseEntity<Parent> updateParent(@PathVariable Long parentId, @RequestBody Parent details) {
        return parentRepository.findById(parentId).map(parent -> {
            parent.setParentName(details.getParentName());
            parent.setPhone(details.getPhone());
            parent.setOccupation(details.getOccupation());
            return ResponseEntity.ok(parentRepository.save(parent));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. DELETE Parent
    @DeleteMapping("/{parentId}")
    public ResponseEntity<?> deleteParent(@PathVariable Long parentId) {
        return parentRepository.findById(parentId).map(parent -> {
            parentRepository.delete(parent);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}