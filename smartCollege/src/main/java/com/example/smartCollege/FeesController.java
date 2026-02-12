package com.example.smartCollege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fees")
@CrossOrigin(origins = "*") // <--- Add this line to ALL Controllers
public class FeesController {

    @Autowired
    private FeesService feesService;

    @PostMapping // This handles the POST request to /api/fees
    public ResponseEntity<Fees> createFees(@RequestBody Fees fees) {
        return ResponseEntity.ok(feesService.saveFees(fees));
    }
    // payfees: Update the paid amount
    @PutMapping("/{feesId}/pay")
    public ResponseEntity<Fees> payFees(@PathVariable Long feesId, @RequestParam Double amount) {
        return ResponseEntity.ok(feesService.processPayment(feesId, amount));
    }

    // checkremaining: See total balance for a student
    @GetMapping("/student/{studId}/remaining")
    public ResponseEntity<Double> checkRemaining(@PathVariable Long studId) {
        return ResponseEntity.ok(feesService.getRemainingBalance(studId));
    }

    // duefees: Get all fee records that have an outstanding balance
    @GetMapping("/overdue")
    public ResponseEntity<List<Fees>> getDueFees() {
        return ResponseEntity.ok(feesService.getAllOverdueFees());
    }

    // viewreceipt: View full details of a fee record
    @GetMapping("/{feesId}/receipt")
    public ResponseEntity<Fees> viewReceipt(@PathVariable Long feesId) {
        return ResponseEntity.ok(feesService.getFeeDetails(feesId));
    }
}