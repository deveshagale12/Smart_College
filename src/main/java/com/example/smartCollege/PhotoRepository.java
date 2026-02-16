package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PhotoRepository extends JpaRepository<StudentPhoto, Long> {
    Optional<StudentPhoto> findByStudentStudId(Long studId);
}