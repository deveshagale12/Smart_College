package com.example.smartCollege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScholarshipService {

    @Autowired
    private ScholarshipRepository scholarshipRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Scholarship applyForScholarship(Long studId, Scholarship scholarship) {
        return studentRepository.findById(studId).map(student -> {
            scholarship.setStudent(student);
            scholarship.setStatus("PENDING"); // Initial status
            return scholarshipRepository.save(scholarship);
        }).orElseThrow(() -> new RuntimeException("Student not found with id " + studId));
    }

    public Scholarship approveScholarship(Long scholarshipId) {
        Scholarship scholarship = scholarshipRepository.findById(scholarshipId)
            .orElseThrow(() -> new RuntimeException("Scholarship not found"));
        
        scholarship.setStatus("APPROVED");
        return scholarshipRepository.save(scholarship);
    }

    public List<Scholarship> getScholarshipsByStudent(Long studId) {
        return scholarshipRepository.findByStudent_StudId(studId);
    }
}