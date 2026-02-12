package com.example.smartCollege;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/college/students")
@CrossOrigin(origins = "*") // Allows frontend to access the API
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // 1. REGISTER (Create Student)
    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // 2. LOGIN (Basic Check)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password"); // This would be the phone number in your logic

        return studentRepository.findByEmail(email)
                .filter(student -> student.getPassword().equals(password))
                .map(student -> ResponseEntity.ok("Login Successful for: " + student.getFullName()))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials"));
    }

    // 3. GET ALL STUDENTS
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // 4. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. UPDATE STUDENT
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentRepository.findById(id).map(student -> {
            student.setFullName(studentDetails.getFullName());
            student.setEmail(studentDetails.getEmail());
            student.setPhoneNo(studentDetails.getPhoneNo());
            student.setAddress(studentDetails.getAddress());
            student.setCourse(studentDetails.getCourse());
            student.setYear(studentDetails.getYear());
         
            
            Student updatedStudent = studentRepository.save(student);
            return ResponseEntity.ok(updatedStudent);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 6. DELETE STUDENT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}