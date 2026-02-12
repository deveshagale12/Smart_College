package com.example.smartCollege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarksService {

    @Autowired
    
    private MarksRepository marksRepository;

    public Marks saveMarks(Marks marks) {
        // Calculate the grade before saving
        marks.setGrade(calculateGrade(marks.getInternalMarks(), marks.getExternalMarks()));
        return marksRepository.save(marks);
    }

    public List<Marks> getMarksByStudentId(Long studentId) {
        return marksRepository.findByStudent_StudId(studentId);
    }

    private String calculateGrade(Integer internal, Integer external) {
        int total = (internal != null ? internal : 0) + (external != null ? external : 0);
        
        if (total >= 90) return "A+";
        if (total >= 80) return "A";
        if (total >= 70) return "B";
        if (total >= 60) return "C";
        if (total >= 50) return "D";
        return "F";
    }
}