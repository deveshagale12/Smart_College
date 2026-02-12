package com.example.smartCollege;

import jakarta.persistence.*;

@Entity
public class Marks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marksId;
    
    private String subject;
    private Integer internalMarks;
    private Integer externalMarks;
    private String grade;

    @ManyToOne
    @JoinColumn(name = "stud_id")
    private Student student;

	public Long getMarksId() {
		return marksId;
	}

	public void setMarksId(Long marksId) {
		this.marksId = marksId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getInternalMarks() {
		return internalMarks;
	}

	public void setInternalMarks(Integer internalMarks) {
		this.internalMarks = internalMarks;
	}

	public Integer getExternalMarks() {
		return externalMarks;
	}

	public void setExternalMarks(Integer externalMarks) {
		this.externalMarks = externalMarks;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
    
    
}