package com.smartCollege;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
@Service
public class FileService {

    // This is the folder where photos will be stored on your computer
    private final String uploadDir = "user-photos/";

    public String saveFile(MultipartFile file, Long studId) throws IOException {
        // 1. Create the directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 2. Create a unique filename (e.g., "101_profile.jpg")
        // This prevents different students from overwriting each other's photos
        String fileName = studId + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 3. Save the file to the folder
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // Return the name so the PhotoService can save it in the DB
    }
    
}