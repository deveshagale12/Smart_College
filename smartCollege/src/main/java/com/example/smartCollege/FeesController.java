package com.example.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/college/fees")
@CrossOrigin(origins = "*")
public class FeesController {

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PaymentRecordRepository paymentRepo; // You'll need to create this simple interface

    // 1. POST a new payment to the list
    @PostMapping("/{studId}/pay")
    public ResponseEntity<Fees> addPayment(@PathVariable Long studId, @RequestParam double amount) {
        return studentRepository.findById(studId).map(student -> {
            
            // Find existing record or create a blank one (No hardcoded 100,000)
            Fees fee = feesRepository.findByStudentStudId(studId).orElseGet(() -> {
                Fees newFee = new Fees();
                newFee.setStudent(student);
                newFee.setTotalAmount(0.0); // Starts at 0
                newFee.setExamFees(0.0);    // Starts at 0
                newFee.setPaidAmount(0.0);
                return feesRepository.save(newFee);
            });

            PaymentRecord record = new PaymentRecord();
            record.setAmountPaid(amount);
            record.setPaymentDate(LocalDateTime.now());
            record.setFees(fee);
            
            fee.getPaymentHistory().add(record);
            fee.calculateDue(); // Calculates (Total + Exam) - History
            
            return ResponseEntity.ok(feesRepository.save(fee));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 2. DELETE a specific payment from the list
    @DeleteMapping("/payment/{paymentId}")
    public ResponseEntity<?> deletePayment(@PathVariable Long paymentId) {
        return paymentRepo.findById(paymentId).map(record -> {
            Fees fee = record.getFees();
            
            // Remove the record from the database
            paymentRepo.delete(record);
            
            // Refresh the fee object from database and recalculate
            fee.getPaymentHistory().remove(record);
            fee.calculateDue();
            
            feesRepository.save(fee);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
    // 1. GET ALL FEES
    @GetMapping("/all")
    public List<Fees> getAllFees() {
        return feesRepository.findAll();
    }

    // 2. GET FEES BY STUDENT ID
    @GetMapping("/student/{studId}")
    public ResponseEntity<Fees> getFeesByStudent(@PathVariable Long studId) {
        return feesRepository.findByStudentStudId(studId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. GET REMAINING BALANCE (SPECIFIC STUDENT)
    @GetMapping("/balance/{studId}")
    public ResponseEntity<Double> getRemainingBalance(@PathVariable Long studId) {
        return feesRepository.findByStudentStudId(studId)
                .map(f -> ResponseEntity.ok(f.getDueAmount()))
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. GET ALL OVERDUES (LIST OF STUDENTS WITH DEBT)
    @GetMapping("/overdues")
    public List<Fees> getOverdues() {
        return feesRepository.findAllOverdueFees();
    }

    // 5. POST/UPDATE FEES (Logic to add payment)
	/*
	 * @PostMapping("/{studId}/pay") public ResponseEntity<Fees>
	 * processPayment(@PathVariable Long studId, @RequestParam double amount) { //
	 * 1. Check if the student exists in the system first return
	 * studentRepository.findById(studId).map(student -> {
	 * 
	 * // 2. Find existing fee record or create a brand new one Fees fee =
	 * feesRepository.findByStudentStudId(studId).orElse(new Fees());
	 * 
	 * if (fee.getFeesId() == null) { // First time setup for this student
	 * fee.setStudent(student); fee.setTotalAmount(100000.0); // Set your default
	 * course fee here fee.setExamFees(5000.0); // Set your default exam fee
	 * fee.setPaidAmount(0.0); }
	 * 
	 * // 3. Apply the payment fee.setPaidAmount(fee.getPaidAmount() + amount);
	 * 
	 * // 4. Recalculate and Save fee.calculateDue(); Fees savedFee =
	 * feesRepository.save(fee);
	 * 
	 * return ResponseEntity.ok(savedFee);
	 * 
	 * }).orElse(ResponseEntity.notFound().build()); }
	 */
    @PutMapping("/update-billing/{studId}")
    public ResponseEntity<Fees> updateBilling(@PathVariable Long studId, @RequestBody Fees details) {
        return feesRepository.findByStudentStudId(studId).map(fee -> {
            // Update the specific fields
            fee.setTotalAmount(details.getTotalAmount());
            fee.setExamFees(details.getExamFees()); // Updating examFees here
            fee.setPaidAmount(details.getPaidAmount());
            
            // Recalculate the due balance automatically
            fee.calculateDue(); 
            
            Fees updatedFee = feesRepository.save(fee);
            return ResponseEntity.ok(updatedFee);
        }).orElse(ResponseEntity.notFound().build());
    }
}