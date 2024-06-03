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

//    @Valid 가 붙었지만 bindresult 로 잡지 않은 에러 공통 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {

        System.out.println("------------------- Controller Advice ----------------------");

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateFriendRequestException.class)
    public HttpStatus DuplicateFriendRequestException(DuplicateFriendRequestException e) {

        //apiResponse 로 에러 메세지까지 보내야 한다
        return HttpStatus.BAD_REQUEST;
    }

}
