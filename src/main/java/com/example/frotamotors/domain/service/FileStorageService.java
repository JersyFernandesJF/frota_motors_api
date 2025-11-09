package com.example.frotamotors.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  @Value("${file.upload-dir:uploads}")
  private String uploadDir;

  public String storeFile(MultipartFile file) throws IOException {
    // Validate file
    if (file.isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    }

    // Validate file type
    String contentType = file.getContentType();
    if (contentType == null
        || (!contentType.startsWith("image/") && !contentType.startsWith("video/"))) {
      throw new IllegalArgumentException("Only image and video files are allowed");
    }

    // Validate file size (10MB max)
    if (file.getSize() > 10 * 1024 * 1024) {
      throw new IllegalArgumentException("File size exceeds 10MB limit");
    }

    // Create upload directory if it doesn't exist
    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    // Generate unique filename
    String originalFilename = file.getOriginalFilename();
    String extension = "";
    if (originalFilename != null && originalFilename.contains(".")) {
      extension = originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    String filename = UUID.randomUUID().toString() + extension;

    // Save file
    Path filePath = uploadPath.resolve(filename);
    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    // Return URL path
    return "/uploads/" + filename;
  }

  public void deleteFile(String fileUrl) throws IOException {
    if (fileUrl == null || !fileUrl.startsWith("/uploads/")) {
      return;
    }

    String filename = fileUrl.substring("/uploads/".length());
    Path filePath = Paths.get(uploadDir).resolve(filename);

    if (Files.exists(filePath)) {
      Files.delete(filePath);
    }
  }
}

