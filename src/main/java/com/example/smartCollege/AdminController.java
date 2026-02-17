package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminRepository adminRepo;

    // 1. REGISTER ADMIN
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        if (adminRepo.findByEmail(admin.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Admin email already exists!");
        }
        Admin savedAdmin = adminRepo.save(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    // 2. LOGIN ADMIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        return adminRepo.findByEmail(email)
            .<ResponseEntity<?>>map(admin -> {
                if (admin.getPassword().equals(password)) {
                    return ResponseEntity.ok(admin);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
                }
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found"));
    }
}