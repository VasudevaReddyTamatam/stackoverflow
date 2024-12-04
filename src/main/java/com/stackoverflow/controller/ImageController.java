package com.stackoverflow.controller;
import com.stackoverflow.model.Image;
import com.stackoverflow.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public String showfrom(){
        return "Image";
    }

    // Endpoint for uploading an image
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("image") MultipartFile file) {
        try {
            imageService.uploadImage(file); // Save image to database
            return "dashboard";
        } catch (IOException e) {
            return "Image";
        }
    }

    @GetMapping("/{id}")
    public String getImage(@PathVariable Long id, Model model) {
        Image image = imageService.getImage(id);
        if (image != null) {
            // Pass the image data and other properties to the template
            model.addAttribute("image", image);
            return "ShowImage"; // Return a view to display the image
        } else {
            model.addAttribute("error", "Image not found.");
            return "Image"; // Return error view if the image isn't found
        }
    }

    @GetMapping("/getImage/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImageData(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        if (image != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", image.getType())  // Set content type to the image type
                    .body(image.getData());  // Return the image byte array as the response
        } else {
            return ResponseEntity.notFound().build();  // Return 404 if the image is not found
        }
    }
}

