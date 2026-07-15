package com.example.demo.util;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;

public class FileUploadUtil {

    public static String uploadImage(Part filePart, ServletContext context) throws Exception {

        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new Exception("Only image files are allowed");
        }

  
        String submitted = Paths.get(filePart.getSubmittedFileName())
                .getFileName().toString();

        String safeName = submitted.replaceAll("[\\\\/:*?\"<>|]", "_");

        // rename กันชื่อซ้ำ
        String ext = "";
        int dot = safeName.lastIndexOf(".");
        if (dot >= 0) {
            ext = safeName.substring(dot);
        }

        String newFileName = System.currentTimeMillis() + ext;

        // path: /images
        String uploadPath = context.getRealPath("/images");
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        Path target = Paths.get(uploadPath, newFileName);

        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }

        return newFileName; 
    }
}
