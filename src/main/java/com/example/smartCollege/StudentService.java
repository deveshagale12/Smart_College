package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;
    
    @Autowired
    private EmailService emailService;

    public Student registerStudent(Student student) {
        // 1. Check if the email is already in the database
        Optional<Student> existingStudent = repository.findByEmail(student.getEmail());
        
        if (existingStudent.isPresent()) {
            // This will trigger the GlobalExceptionHandler we discussed
            throw new EmailAlreadyExistsException("The email " + student.getEmail() + " is already in use.");
        }

        // 2. If unique, proceed to save
        Student savedStudent = repository.save(student);
        
        // 3. Trigger the success email
        emailService.sendRegistrationEmail(savedStudent.getEmail(), savedStudent.getFullName());
        
        return savedStudent;
    }
   
    public class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }
  
    
 // LOGIN FUNCTION
    public Student login(String email, String password) {
        return repository.findByEmail(email)
                .filter(s -> s.getPassword().equals(password)) // In real apps, use BCrypt!
                .orElseThrow(() -> new RuntimeException("Invalid Email or Password"));
    }

    // GET ALL
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // GET BY ID
    public Student getStudentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }

    // DELETE
    public void deleteStudent(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Student ID " + id + " not found.");
        }
        repository.deleteById(id);
    }

    // EDIT / UPDATE
    public Student updateStudent(Long id, Student updatedDetails) {
        Student existingStudent = getStudentById(id);
        
        existingStudent.setFullName(updatedDetails.getFullName());
        existingStudent.setPhoneNo(updatedDetails.getPhoneNo());
        existingStudent.setAddress(updatedDetails.getAddress());
        existingStudent.setCourse(updatedDetails.getCourse());
        existingStudent.setYear(updatedDetails.getYear());
        // We usually don't update email/password here for security reasons
        
        return repository.save(existingStudent);
    }
}