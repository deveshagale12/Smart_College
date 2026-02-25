package com.smartCollege;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*; // Covers @Entity, @Id, @ManyToOne, etc.
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.time.LocalDateTime;
@Entity
@Table(name = "faculty_notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String fileName;
    private String fileType;
    private LocalDateTime uploadDate;

    @Lob
    @Column(name = "note_data")
    @com.fasterxml.jackson.annotation.JsonIgnore // 1. Prevents sending raw file bytes in the history list
    private byte[] data; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stud_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("notes") // 2. Breaks recursion: includes Student info but ignores their note list
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties("notes") // 3. Breaks recursion: includes Faculty info but ignores their note list
    private Faculty faculty;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public LocalDateTime getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDateTime uploadDate) {
		this.uploadDate = uploadDate;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

    // Standard Getters and Setters
    
    
}