package com.example.demo;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadUtil {

    public static String cleanFileName(String fileName) {
        if (fileName == null) return null;
        // Remove path traversal sequences and normalize
        fileName = Paths.get(fileName).getFileName().toString();
        if (fileName.contains("..")) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }
        return fileName;
    }

    public static Path getUploadPath() {
        // Example upload folder path (can be externalized)
        return Paths.get("uploads").toAbsolutePath().normalize();
    }
}
