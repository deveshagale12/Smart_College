package com.smartCollege;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    // For Student: "Show me all notes sent to me"
    List<Note> findByStudentStudIdOrderByUploadDateDesc(Long studId);

    // For Faculty: "Show me all notes I have sent"
    List<Note> findByFacultyId(Long facultyId);
    List<Note> findByFacultyIdOrderByUploadDateDesc(Long facultyId);
}