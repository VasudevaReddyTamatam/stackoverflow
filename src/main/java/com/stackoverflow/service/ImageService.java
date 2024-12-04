package com.stackoverflow.service;

import com.stackoverflow.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Image uploadImage(MultipartFile file) throws IOException;
    Image getImage(Long id);
}
