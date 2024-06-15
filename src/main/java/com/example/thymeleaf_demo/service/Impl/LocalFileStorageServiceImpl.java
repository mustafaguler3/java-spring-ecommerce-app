package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.exception.FileNotFoundException;
import com.example.thymeleaf_demo.exception.FileStorageException;
import com.example.thymeleaf_demo.service.FileStorageService;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final Path fileLocation;

    public LocalFileStorageServiceImpl(@Value("${upload.dir}") String uploadDir) {
        this.fileLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            if (!Files.exists(fileLocation)) {
                Files.createDirectories(this.fileLocation);
            }
        }catch (Exception e){
            throw new FileStorageException("Could not create directory");
        }
    }

    @Override
    public String storeFile(MultipartFile file) {
        String fileName =
                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;

        try {
            if(fileName.contains("..")){
                throw new FileStorageException("File name contains invalid path sequence"+fileName);
            }
            Path targetLocation = this.fileLocation.resolve(fileName).toAbsolutePath().normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        }catch (IOException e){
            throw new FileStorageException("Could not store file "+fileName + " please try again");
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).toAbsolutePath().normalize();

            Resource resource = new UrlResource(filePath.toUri());
            System.out.println("File path: " + filePath.toString());

            if(resource.exists() || resource.isReadable()){
                return resource;
            }else {
                throw new FileStorageException("File not found " + filePath);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("file not found " + fileName,e);
        }
    }

    @Override
    public Path loadFile(String fileName) {
        Path filePath = this.fileLocation.resolve(fileName).normalize();
        return filePath;
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        }catch (IOException ex){
            throw new FileStorageException("Could not delete file "+fileName + " please try again");
        }
    }
}


