package com.dbproject.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

// Send to error page when unexpected value is entered in parameter
@ControllerAdvice
@Slf4j
public class ErrorPageControllerAdvice {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {

        log.info("----error page controller advice 0------");

        model.addAttribute("errormessage", ex.getMessage());

        return "error/errorPage";
    }
}
