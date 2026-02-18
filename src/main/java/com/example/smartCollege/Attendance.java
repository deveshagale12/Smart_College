package com.smartCollege;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendId;

    private LocalDate date;
    private LocalTime time;
    
    // Status can be: PRESENT, ABSENT, LATE
    private String status;

    // Location fields
    private Double latitude;
    private Double longitude;
    private String address; // Optional: Store a readable address string

    @ManyToOne
    @JoinColumn(name = "stud_id")
    @JsonIgnoreProperties({"fees", "academicRecords", "password"}) // Blocks recursion but keeps name
    private Student student;

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

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

    // Standard Getters and Setters
    
    
    
}