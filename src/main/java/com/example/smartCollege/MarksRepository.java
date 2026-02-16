package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudentStudId(Long studId);
}