package com.smartCollege;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*; // Covers @Entity, @Id, @ManyToOne, etc.
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studId;
    
    private String fullName;
    private LocalDate dob;
    @Column(unique = true, nullable = false)
    private String email;
    private String password; 
    private String phoneNo;
    private String address;
    private String course;
    private Integer year;
    
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    @JsonIgnoreProperties("students") // This stops the faculty from trying to re-serialize this student list
    private Faculty faculty;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcademicRecord> academicRecords;

    // 2. Link to Student Photo (since you have PhotoService)
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private StudentPhoto photo;
    
	public Long getStudId() {
		return studId;
	}
	public void setStudId(Long studId) {
		this.studId = studId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Faculty getFaculty() {
		return faculty;
	}
	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}
    
    
}