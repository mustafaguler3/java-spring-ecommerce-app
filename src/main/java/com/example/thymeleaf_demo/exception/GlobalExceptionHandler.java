package com.example.thymeleaf_demo.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ModelAndView handleFileNotFoundException(FileNotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(FileStorageException.class)
    public ModelAndView handleFileStorageException(FileStorageException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}



















