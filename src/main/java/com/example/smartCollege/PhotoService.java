package com.smartCollege;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private StudentRepository studentRepository;

    // Combined Upload/Update Logic
    public StudentPhoto updatePhoto(Long studId, MultipartFile file) throws IOException {
        // 1. Find the student
        Student student = studentRepository.findById(studId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Check if a record already exists, if not, create one
        StudentPhoto photo = photoRepository.findByStudentStudId(studId)
                .orElse(new StudentPhoto());

        // 3. Set the binary data and metadata
        photo.setData(file.getBytes()); // This saves directly to Neon DB
        photo.setFileType(file.getContentType());
        photo.setStudent(student);

        return photoRepository.save(photo);
    }

    public void deletePhoto(Long studId) {
        StudentPhoto photo = photoRepository.findByStudentStudId(studId)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
        
        // Just delete from DB - no physical file cleanup needed!
        photoRepository.delete(photo);
    }

    public StudentPhoto getPhotoByStudentId(Long studId) {
        return photoRepository.findByStudentStudId(studId)
                .orElseThrow(() -> new RuntimeException("No photo found for ID: " + studId));
    }
    public StudentPhoto uploadPhoto(Long studId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentPhoto photo = photoRepository.findByStudentStudId(studId)
                .orElse(new StudentPhoto());

        // These methods are now all defined:
        photo.setFileName(file.getOriginalFilename()); // This was causing your error
        photo.setFileType(file.getContentType());
        photo.setData(file.getBytes());
        photo.setStudent(student);

        return photoRepository.save(photo);
    }
}