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
    
 // ADD THIS: Link to the Fees entity
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Fees fees;

    // 2. Link to Student Photo (since you have PhotoService)
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private StudentPhoto photo;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore // Prevent Notes -> Student -> Attendance loop
    private List<Attendance> attendance;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore // Prevent Notes -> Student -> Marks loop
    private List<Marks> marks;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore // Prevent Notes -> Student -> Parent loop
    private List<Parent> parents;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore // Prevent Notes -> Student -> Records loop
    private List<AcademicRecord> academicRecords;

	// Inside Student.java
@OneToMany(mappedBy = "student")
@JsonIgnore // This prevents the Student from trying to list their notes again
private List<Note> notes;
    
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