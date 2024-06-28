package com.example.thymeleaf_demo.exception;

import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.UserService;
import groovy.util.logging.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    private final UserService userService;

    @Autowired
    public GlobalExceptionHandler(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addUserToModel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            UserDto currentUser = userService.findByUsername(currentUsername);
            logger.info("User from global ex {}", currentUser);
            model.addAttribute("currentUser", currentUser);
        }
    }

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


    @ExceptionHandler(SpelEvaluationException.class)
    public ModelAndView handleSpelEvaluationException(SpelEvaluationException exception) {
        ModelAndView modelAndView = new ModelAndView();

        if (exception.getStackTrace().length > 0){
            StackTraceElement element = exception.getStackTrace()[0];
            logger.error("Exp occurred in class: {}, method: {}, line: {}",
                    element.getClassName(), element.getMethodName(), element.getLineNumber());
        }
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}



















