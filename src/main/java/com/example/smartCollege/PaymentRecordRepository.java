package com.example.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    // Basic CRUD operations are inherited
}