package com.smartCollege;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
@Entity
@Table(name = "payment_records")
public class PaymentRecord {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double amountPaid;
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "fees_id")
    @JsonIgnore // Important to prevent infinite loops in JSON
    private Fees fees;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Fees getFees() {
		return fees;
	}

	public void setFees(Fees fees) {
		this.fees = fees;
	}

    
    
    // Getters and Setters
}