package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/faculty")
@CrossOrigin(origins = "*")
public class FacultyController {

    @Autowired
    private FacultyRepository facultyRepo;
    
    @Autowired
    private StudentRepository studentRepo;

    @PostMapping("/register")
    public Faculty register(@RequestBody Faculty faculty) {
        // In a real app, encode password before saving
        return facultyRepo.save(faculty);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Faculty loginData) {
        return facultyRepo.findByEmail(loginData.getEmail())
            .<ResponseEntity<?>>map(faculty -> {
                // Check password
                if (faculty.getPassword().equals(loginData.getPassword())) {
                    return ResponseEntity.ok(faculty); // Returns Faculty Object
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                         .body("Invalid Credentials"); // Returns String
                }
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                           .body("Faculty email not registered"));
    }
    @PostMapping("/{facultyId}/assign-students")
    public String assignStudentsToFaculty(
            @PathVariable Long facultyId, 
            @RequestBody List<Long> studentIds) {
        
        // 1. Find the Faculty
        Faculty faculty = facultyRepo.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found with ID: " + facultyId));

        // 2. Find all Students in the group
        List<Student> students = studentRepo.findAllById(studentIds);

        // 3. Assign Faculty to each Student
        for (Student student : students) {
            student.setFaculty(faculty);
        }

        // 4. Save updated students
        studentRepo.saveAll(students);

        return "Successfully assigned " + students.size() + " students to Faculty: " + faculty.getName();
    }
   
    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getFacultyStudents(@PathVariable Long id) {
        return facultyRepo.findById(id)
            .map(faculty -> ResponseEntity.ok(faculty.getStudents()))
            .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}/update-profile")
    public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Faculty updatedData) {
        return facultyRepo.findById(id).map(faculty -> {
            // ONLY update the basic fields. DO NOT touch the students list here.
            faculty.setName(updatedData.getName());
            faculty.setSubject(updatedData.getSubject());
            faculty.setOccupation(updatedData.getOccupation());
            faculty.setSalary(updatedData.getSalary());
            faculty.setFacultyId(updatedData.getFacultyId()); // Ensure the custom ID is updated
            
            if (updatedData.getPassword() != null && !updatedData.getPassword().isEmpty()) {
                faculty.setPassword(updatedData.getPassword());
            }
            
            Faculty savedFaculty = facultyRepo.save(faculty);
            return ResponseEntity.ok(savedFaculty);
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        return facultyRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

	/*
	 * @GetMapping public List<Faculty> getAllFaculties() { return
	 * facultyRepo.findAll(); }
	 */
    @GetMapping
    @Transactional(readOnly = true) // Ensures the database session stays open
    public List<Faculty> getAllFaculties() {
        List<Faculty> faculties = facultyRepo.findAll();
        // Force initialization of the proxy collection
        faculties.forEach(f -> {
            if (f.getStudents() != null) {
                f.getStudents().size(); // Calling size() forces Hibernate to load the data
            }
        });
        return faculties;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFaculty(@PathVariable Long id) {
        return facultyRepo.findById(id).map(faculty -> {
            // Optional: Check if faculty has students before deleting
            if (!faculty.getStudents().isEmpty()) {
                return ResponseEntity.badRequest().body("Cannot delete faculty with assigned students.");
            }
            facultyRepo.delete(faculty);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> resetData) {
        String email = resetData.get("email");
        String newPassword = resetData.get("password");

        return facultyRepo.findByEmail(email).map(faculty -> {
            faculty.setPassword(newPassword); // Reminder: Use password encoding in production!
            facultyRepo.save(faculty);
            return ResponseEntity.ok("Password updated successfully");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No faculty found with this email."));
    }
}