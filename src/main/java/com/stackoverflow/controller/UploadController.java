//package com.stackoverflow.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@Controller
//@RequestMapping("/uploads")
//public class UploadController {
//
//    private final String uploadDir = "uploads/";
//
//    private boolean isImageFile(MultipartFile file) {
//        String contentType = file.getContentType();
//        return contentType != null &&
//                (contentType.equals("image/jpeg") ||
//                        contentType.equals("image/png") ||
//                        contentType.equals("image/gif"));
//    }
//
//    @PostMapping("/image")
//    @ResponseBody
//    public ResponseEntity<Object> uploadImage(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("File is empty");
//        }
//
//        if (!isImageFile(file)) {
//            return ResponseEntity.badRequest().body("Invalid file type. Only image files are allowed.");
//        }
//
//        try {
//            String sanitizedFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
//            String fileName = System.currentTimeMillis() + "_" + sanitizedFileName;
//            Path filePath = Paths.get(uploadDir + fileName);
//            Files.createDirectories(filePath.getParent());
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            String imageUrl = ServletUriComponentsBuilder
//                    .fromCurrentContextPath()
//                    .path("/uploads/")
//                    .path(fileName)
//                    .toUriString();
//            return ResponseEntity.ok().body("{\"location\": \"" + imageUrl + "\"}");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
//        }
//    }
//}