package com.smartCollege;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "student_photo")
public class StudentPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType; // image/png, etc.

    private String fileName; // ADD THIS BACK
    @Lob // Tells JPA to use a Large Object (bytea in Postgres)
    @Column(name = "photo_data")
    private byte[] data;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "studId")
    @JsonIgnore // Prevent circular reference in JSON
    private Student student;
    
    

    // Getters and Setters
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
    // ... keep other existing getters/setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
    
}