package com.stackoverflow.controller;
import com.stackoverflow.model.Image;
import com.stackoverflow.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/uploads")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Long imageId = imageService.uploadImage(file).getId();
            String imageUrl = "/uploads/image/" + imageId;
            return ResponseEntity.ok(Map.of("location", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image: " + e.getMessage()));
        }
    }


    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImageData(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        if (image != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", image.getType())
                    .body(image.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}

