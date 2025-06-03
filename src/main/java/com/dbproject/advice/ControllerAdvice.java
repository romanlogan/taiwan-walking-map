package com.dbproject.advice;

import com.dbproject.api.ApiResponse;
import com.dbproject.binding.ErrorDetail;
import com.dbproject.exception.DuplicateFriendRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class ControllerAdvice {

//    Common error handling that is marked with @Valid but not caught by bindresult
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {

        Map<String, ErrorDetail> errorMap = new HashMap<>();

        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {

            System.out.println("field name : " + fieldError.getField() + ", value = " + fieldError.getRejectedValue());
            ErrorDetail errorDetail = new ErrorDetail(fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            errorMap.put(fieldError.getField(), errorDetail);
        }

        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                null,
                errorMap
        );
    }
}
