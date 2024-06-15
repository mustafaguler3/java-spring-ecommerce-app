package com.example.thymeleaf_demo.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIR = "uploads";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
    //converts the uploadDir string to a path
    //maps requests starting with "/photos/ to a file system location"
    //file:<absoulute path to photos directory>
    //The ** will match on all sub-directories
    private void exposeDirectory(String uploadDir,ResourceHandlerRegistry registry){
        Path path = Paths.get(uploadDir);
        registry.addResourceHandler("/"+uploadDir+"/**")
                .addResourceLocations("file:"+path.toAbsolutePath()+"/");
    }
}
