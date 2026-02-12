package com.example.smartCollege;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudent_StudId(Long studId);
}