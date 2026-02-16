package com.example.smartCollege;

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
    @Autowired
    private FileService fileService; // The service we created in the previous step

    public StudentPhoto uploadPhoto(Long studId, MultipartFile file) throws IOException {
        // 1. Find the student
        Student student = studentRepository.findById(studId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // 2. Save the physical file
        String fileName = fileService.saveFile(file, studId);

        // 3. Check if photo record already exists, or create new one
        StudentPhoto photo = photoRepository.findByStudentStudId(studId)
                .orElse(new StudentPhoto());

        photo.setFileName(fileName);
        photo.setFileType(file.getContentType());
        photo.setStudent(student); // Link the photo to the student

        return photoRepository.save(photo);
    }
    
 // FETCH: Get photo details by Student ID
    public StudentPhoto getPhotoByStudentId(Long studId) {
        return photoRepository.findByStudentStudId(studId)
                .orElseThrow(() -> new RuntimeException("Photo not found for Student ID: " + studId));
    }

    public StudentPhoto updatePhoto(Long studId, MultipartFile file) throws IOException {
        // 1. Find the existing database record
        StudentPhoto existingPhoto = photoRepository.findByStudentStudId(studId)
                .orElseThrow(() -> new RuntimeException("No existing photo to update."));

        // 2. Delete the OLD physical file from the folder
        Path oldFilePath = Paths.get("user-photos/" + existingPhoto.getFileName());
        try {
            Files.deleteIfExists(oldFilePath);
        } catch (IOException e) {
            // We log the error but continue so the user can still upload the new photo
            System.out.println("Warning: Could not delete old file: " + e.getMessage());
        }

        // 3. Save the NEW physical file
        String newFileName = fileService.saveFile(file, studId);

        // 4. Update the database record with the new details
        existingPhoto.setFileName(newFileName);
        existingPhoto.setFileType(file.getContentType());

        return photoRepository.save(existingPhoto);
    }
    public void deletePhoto(Long studId) throws IOException {
        // 1. Find the photo record in the database
        StudentPhoto photo = photoRepository.findByStudentStudId(studId)
                .orElseThrow(() -> new RuntimeException("Photo not found for student ID: " + studId));

        // 2. Construct the path to the physical file
        Path filePath = Paths.get("user-photos/" + photo.getFileName());

        // 3. Delete the file from the folder
        Files.deleteIfExists(filePath);

        // 4. Delete the record from the database
        photoRepository.delete(photo);
    }
}