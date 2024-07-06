package com.example.thymeleaf_demo.exception;

import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.CartService;
import com.example.thymeleaf_demo.service.UserService;
import groovy.util.logging.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    private final UserService userService;
    private final CartService cartService;

    @Autowired
    public GlobalExceptionHandler(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @ModelAttribute
    public void cartCount(Principal principal, Model model){
        if (principal!=null){
            String username = principal.getName();
            UserDto userDto = userService.findByUsername(username);

            int cartItemCount = cartService.getCartItemCountByUserId(userDto.getId());

            model.addAttribute("cartItemCount",cartItemCount);
        }
    }

    @ModelAttribute
    public void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            UserDto currentUser = userService.findByUsername(username);
            model.addAttribute("currentUser", currentUser);
        }
    }
    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, WebRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("message", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleResourceNotFoundException(MethodArgumentTypeMismatchException exception) {
        ModelAndView modelAndView = new ModelAndView();

        if (exception.getStackTrace().length > 0){
            StackTraceElement element = exception.getStackTrace()[0];
            logger.error("Exception occurred in class: {}, method: {}, line: {}",
                    element.getClassName(), element.getMethodName(), element.getLineNumber());
        }
        modelAndView.addObject("message",exception.getMessage());
        modelAndView.addObject("exception",exception.fillInStackTrace());
        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView();

        if (exception.getStackTrace().length > 0){
            StackTraceElement element = exception.getStackTrace()[0];
            logger.error("Exception occurred in class: {}, method: {}, line: {}",
                    element.getClassName(), element.getMethodName(), element.getLineNumber());
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



















