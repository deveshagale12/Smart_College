package com.smartCollege;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "student_marks")
public class Marks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marksId;

    private String subject;
    
    @Column(name = "test_type") // e.g., Midterm, Final, Quiz
    private String testType;
    
    private double outOf;
    private double obtainedMarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stud_id")
    @JsonIgnore
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

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public double getOutOf() {
		return outOf;
	}

	public void setOutOf(double outOf) {
		this.outOf = outOf;
	}

	public double getObtainedMarks() {
		return obtainedMarks;
	}

	public void setObtainedMarks(double obtainedMarks) {
		this.obtainedMarks = obtainedMarks;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

    // Getters and Setters
    
    
    
}