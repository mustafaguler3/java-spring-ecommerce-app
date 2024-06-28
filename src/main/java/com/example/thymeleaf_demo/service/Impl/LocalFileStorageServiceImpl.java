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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final Path fileLocation;
    //private final Path productStorageLocation = Paths.get("uploads/products");
    //private final Path userStorageLocation = Paths.get("uploads/users");

    public LocalFileStorageServiceImpl(@Value("${upload.dir}") String uploadDir) {
        this.fileLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileLocation.resolve("products"));
            Files.createDirectories(this.fileLocation.resolve("users"));
        }catch (Exception e){
            throw new FileStorageException("Could not create directory");
        }
    }

    @Override
    public String storeFile(MultipartFile file,String fileType) {
        String fileName =
                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //String uniqueFilename = UUID.randomUUID().toString() + "_" + fileName;

        try {
            if(fileName.contains("..")){
                throw new FileStorageException("File name contains invalid path sequence"+fileName);
            }
            Path targetLocation = this.fileLocation.resolve(fileType).resolve(fileName).normalize();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        }catch (IOException e){
            throw new FileStorageException("Could not store file "+fileName + " please try again");
        }
    }



    @Override
    public Resource loadFile(String fileName, String fileType) {
        try {
            Path filePath = this.fileLocation.resolve(fileType).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundException("File not found "+fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /*private Path determineTargetLocation(String fileType){
        if(fileType.equals("products")){
            return productStorageLocation;
        }else if(fileType.equals("users")){
            return userStorageLocation;
        }else {
            throw new FileStorageException("Invalid file type");
        }
    }*/

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


