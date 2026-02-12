package com.example.smartCollege;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeesRepository extends JpaRepository<Fees, Long>{
	List<Fees> findByDueAmountGreaterThan(Double amount);
	Fees findByStudent_StudId(Long studId);
}
