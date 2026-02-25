
package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
@Service
public class NoteService {
    @Autowired private NoteRepository noteRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private FacultyRepository facultyRepository;

    public Note saveNote(Long facultyId, Long studId, String title, MultipartFile file) throws IOException {
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        Student student = studentRepository.findById(studId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Note note = new Note();
        note.setTitle(title);
        note.setFileName(file.getOriginalFilename());
        note.setFileType(file.getContentType());
        note.setData(file.getBytes());
        note.setUploadDate(LocalDateTime.now());
        note.setFaculty(faculty);
        note.setStudent(student);

        return noteRepository.save(note);
    }
@Transactional(readOnly = true)
    public List<Note> getNotesForStudent(Long studId) {
        return noteRepository.findByStudentStudIdOrderByUploadDateDesc(studId);
    }

    public Note getNoteContent(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
    }
 // YOUR MISSING METHOD:
 @Transactional(readOnly = true)
    public List<Note> getNotesByFaculty(Long facultyId) {
        return noteRepository.findByFacultyIdOrderByUploadDateDesc(facultyId);
    }
}