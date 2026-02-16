package com.example.smartCollege;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "student_fees")
public class Fees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feesId;

    private double totalAmount;
    private double paidAmount;
    private double dueAmount;
    private double examFees;
    
    

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stud_id")
    @JsonIgnore
    private Student student;
    @OneToMany(mappedBy = "fees", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PaymentRecord> paymentHistory = new ArrayList<>();
    
    public List<PaymentRecord> getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(List<PaymentRecord> paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public void calculateDue() {
        // Correct way to sum the list of payments
        this.paidAmount = paymentHistory.stream()
                                        .mapToDouble(record -> record.getAmountPaid())
                                        .sum();
                                        
        // Formula: (Total + Exam) - Total Paid from list
        this.dueAmount = (this.totalAmount + this.examFees) - this.paidAmount;
    }
 

	public Long getFeesId() {
		return feesId;
	}

	public void setFeesId(Long feesId) {
		this.feesId = feesId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public double getExamFees() {
		return examFees;
	}

	public void setExamFees(double examFees) {
		this.examFees = examFees;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

    
    // Getters and Setters
}