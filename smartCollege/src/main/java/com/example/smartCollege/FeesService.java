package com.example.smartCollege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeesService {

    @Autowired
    private FeesRepository feesRepository;

    public Fees processPayment(Long feesId, Double amount) {
        Fees fees = feesRepository.findById(feesId)
            .orElseThrow(() -> new RuntimeException("Fee record not found"));

        double newPaidAmount = fees.getPaidAmount() + amount;
        fees.setPaidAmount(newPaidAmount);
        
        // Logic: Due = Total - Paid
        fees.setDueAmount(fees.getTotalAmount() - newPaidAmount);
        
        return feesRepository.save(fees);
    }

    
    public Fees saveFees(Fees fees) {
        // Automatically calculate due amount before saving if not provided
        if (fees.getTotalAmount() != null && fees.getPaidAmount() != null) {
            fees.setDueAmount(fees.getTotalAmount() - fees.getPaidAmount());
        }
        return feesRepository.save(fees);
    }
    public Double getRemainingBalance(Long studId) {
        Fees fees = feesRepository.findByStudent_StudId(studId);
        return fees.getDueAmount();
    }

    public List<Fees> getAllOverdueFees() {
        // Returns all records where dueAmount is greater than 0
        return feesRepository.findByDueAmountGreaterThan(0.0);
    }

    public Fees getFeeDetails(Long feesId) {
        return feesRepository.findById(feesId).get();
    }
}