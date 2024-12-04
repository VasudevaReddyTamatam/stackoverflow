package com.stackoverflow.service;

import com.stackoverflow.model.Image;
import com.stackoverflow.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image uploadImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setData(file.getBytes());

        return imageRepository.save(image); // Save the image to the database
    }

    public Image getImage(Long id) {
        return imageRepository.findById(id).orElse(null); // Retrieve the image from the database
    }
}
