package com.example.thymeleaf_demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView();

        if (exception.getStackTrace().length > 0){
            StackTraceElement element = exception.getStackTrace()[0];
            logger.error("Exception occurred in class: {}, method: {}, line: {}", element.getClassName(), element.getMethodName(), element.getLineNumber());
        }
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.addObject("exception",exception.fillInStackTrace());
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleMethodNotValidException(MethodArgumentNotValidException exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentExceptionException(IllegalArgumentException exception) {
        ModelAndView modelAndView = new ModelAndView();

        if (exception.getStackTrace().length > 0){
            StackTraceElement element = exception.getStackTrace()[0];
            logger.error("Ex occurred in class: {}, method: {}, line: {}",
                    element.getClassName(), element.getMethodName(), element.getLineNumber());
        }
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}



















