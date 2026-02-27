package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/college/students")
@CrossOrigin(origins = "*") // Add this line!
public class StudentController {

    @Autowired
    private StudentService service;
    
    @Autowired
    private PhotoService photoService;
    
    @Autowired
    private StudentRepository studentRepo;
    
   //** */ @PostMapping("/register")
   // public ResponseEntity<?> createStudent(@RequestBody Student student) {
       // try {
        //    Student savedStudent = service.registerStudent(student);
            // Returns 201 Created status, which is more REST-compliant for registration
         //   return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
       // } catch (EmailAlreadyExistsException e) {
            // Returns 409 Conflict with the custom error message
           // return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
      //  } catch (Exception e) {
            // Catches any other unexpected errors
        //    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
      //  }
    //}

    @PostMapping("/register")
public ResponseEntity<?> createStudent(@RequestBody Student student) {
    try {
        // Log the attempt
        System.out.println("Attempting to register student: " + student.getEmail());
        
        Student savedStudent = service.registerStudent(student);
        
        // Log the success
        System.out.println("Student registered successfully: " + savedStudent.getStudId());
        
        // Return 201 Created
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    } catch (EmailAlreadyExistsException e) {
        // Return 409 Conflict
        System.err.println("Registration failed: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    } catch (Exception e) {
        // Return 500 Internal Server Error with specific details
        System.err.println("Unexpected error: " + e.getMessage());
        e.printStackTrace(); // Log full stack trace for debugging
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Registration failed due to a server error.");
    }
}
    public class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }
 // 1. LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            Student student = service.login(loginData.get("email"), loginData.get("password"));
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // 2. GET ALL
    @GetMapping
    public List<Student> getAll() {
        return service.getAllStudents();
    }

    // 3. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudentById(id));
    }

    // 4. UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(service.updateStudent(id, student));
    }

    // 5. DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }
    
    //6. photo upload
    
    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<?> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            StudentPhoto savedPhoto = photoService.uploadPhoto(id, file);
            return ResponseEntity.ok("Photo linked to Student ID " + id + " successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
 // FETCH PHOTO DATA
    @GetMapping("/{id}/photo")
    public ResponseEntity<StudentPhoto> getPhotoDetails(@PathVariable Long id) {
        return ResponseEntity.ok(photoService.getPhotoByStudentId(id));
    }

    // UPDATE PHOTO
    @PutMapping("/{id}/upload-photo") // Using PUT for updates
    public ResponseEntity<?> updateStudentPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            StudentPhoto updated = photoService.updatePhoto(id, file);
            return ResponseEntity.ok("Photo updated successfully: " + updated.getFileName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}/photo")
    public ResponseEntity<String> removePhoto(@PathVariable Long id) {
        try {
            photoService.deletePhoto(id);
            return ResponseEntity.ok("Profile photo deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting photo: " + e.getMessage());
        }
    }
    @GetMapping("/assigned-to/{facultyId}")
    public ResponseEntity<List<Student>> getStudentsByFaculty(@PathVariable Long facultyId) {
        List<Student> students = service.getStudentsByFacultyId(facultyId);
        return ResponseEntity.ok(students);
    }
    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }
    
}