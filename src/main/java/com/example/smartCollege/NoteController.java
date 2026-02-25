package com.SmartCollege2;

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
    public ResponseEntity<List<Note>> getMyNotes(@PathVariable Long studId) {
        return ResponseEntity.ok(noteService.getNotesForStudent(studId));
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
}