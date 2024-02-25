package com.dbproject.advice;

import com.dbproject.api.ApiResponse;
import com.dbproject.exception.DuplicateFriendRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class ControllerAdvice {

//    공통으로 처리
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApiResponse<Object> bindException(BindException e) {

//        다중 에러시 메시지 와 데이터 처리
        List<String> messageList = new ArrayList<>();
        List<Object> dataList = new ArrayList<>();
        List<FieldError> fieldErrors = e.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            System.out.println("field name : " + fieldError.getField() + ", value = " + fieldError.getRejectedValue());

            dataList.add(fieldError.getRejectedValue());
            messageList.add(fieldError.getDefaultMessage());
        }


        return ApiResponse.of(
                HttpStatus.BAD_REQUEST,
                messageList,
                dataList
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateFriendRequestException.class)
    public HttpStatus DuplicateFriendRequestException(DuplicateFriendRequestException e) {

        //apiResponse 로 에러 메세지까지 보내야 한다
        return HttpStatus.BAD_REQUEST;
    }

}
