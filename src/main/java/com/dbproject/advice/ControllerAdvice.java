package com.dbproject.advice;

import com.dbproject.exception.DuplicateFriendRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public HttpStatus bindException(BindException e) {
        return HttpStatus.BAD_REQUEST;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateFriendRequestException.class)
    public HttpStatus DuplicateFriendRequestException(DuplicateFriendRequestException e) {

        //apiResponse 로 에러 메세지까지 보내야 한다
        return HttpStatus.BAD_REQUEST;
    }

}
