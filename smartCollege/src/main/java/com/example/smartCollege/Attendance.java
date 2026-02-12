package com.example.smartCollege;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendId;
    
    private LocalDate date;
    private LocalTime time;
    private String status; // "PRESENT", "ABSENT", "LATE"

    @ManyToOne
    @JoinColumn(name = "stud_id")
    private Student student;

    private Long facultyId; // Assuming faculty is handled by a simple ID for now

	public Long getAttendId() {
		return attendId;
	}

	public void setAttendId(Long attendId) {
		this.attendId = attendId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Long getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(Long facultyId) {
		this.facultyId = facultyId;
	}

    // Getters and Setters
    
    
    
}