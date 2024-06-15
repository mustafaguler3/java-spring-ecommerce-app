package com.example.thymeleaf_demo.service;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    Resource loadFileAsResource(String fileName);
    Path loadFile(String fileName);
    void deleteFile(String fileName);
}
