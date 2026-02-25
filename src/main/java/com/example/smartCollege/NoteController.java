package com.smartCollege;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired private NoteService noteService;

    // 1. POST NOTE (Faculty uses this)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadNote(
            @RequestParam("facultyId") Long facultyId,
            @RequestParam("studId") Long studId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {
        try {
            noteService.saveNote(facultyId, studId, title, file);
            return ResponseEntity.ok("Note shared with student successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 2. GET NOTES LIST (Student uses this to see their inbox)
  
    @GetMapping("/student/{studId}")
public ResponseEntity<?> getMyNotes(@PathVariable Long studId) {
    try {
        List<Note> notes = noteService.getNotesForStudent(studId);
        // If this succeeds, you'll get your list
        return ResponseEntity.ok(notes);
    } catch (Exception e) {
        // If this fails, it will tell us EXACTLY why (e.g., Infinite Recursion)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Backend Error: " + e.getMessage());
    }
}

    // 3. VIEW/DOWNLOAD (Used by Student to open the file)
    @GetMapping("/view/{noteId}")
    public ResponseEntity<byte[]> viewNote(@PathVariable Long noteId) {
        Note note = noteService.getNoteContent(noteId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(note.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + note.getFileName() + "\"")
                .body(note.getData());
    }
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Note>> getNotesByFaculty(@PathVariable Long facultyId) {
        List<Note> notes = noteService.getNotesByFaculty(facultyId);
        return ResponseEntity.ok(notes);
    }
}