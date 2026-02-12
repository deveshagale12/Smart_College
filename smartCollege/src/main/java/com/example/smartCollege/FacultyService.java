package com.example.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private MarksRepository marksRepository;

    public Faculty registerFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public boolean login(String email, String password) {
        return facultyRepository.findByEmail(email)
                .map(f -> f.getPassword().equals(password))
                .orElse(false);
    }

    public void markAttendance(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    public void assignMarks(Marks marks) {
        // Your existing logic to calculate grade can be called here
        marksRepository.save(marks);
    }
}