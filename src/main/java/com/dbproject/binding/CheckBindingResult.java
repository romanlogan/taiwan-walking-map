package com.dbproject.binding;

import com.dbproject.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

public class CheckBindingResult {

    public static ResponseEntity induceSuccessInAjax(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            List<String> messageList = new ArrayList<>();
            List<Object> dataList = new ArrayList<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                dataList.add(fieldError.getRejectedValue());
                messageList.add(fieldError.getDefaultMessage());
            }

//          오류 데이터를 다시 보낼 필요가 없으므로 success 로 유도할 필요 없으므로 BAD_REQUEST
            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    messageList,
                    dataList
            ), HttpStatus.OK);
        }

        // null 이면 에러 없음
        return null;
    }


    public static ResponseEntity induceErrorInAjax(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            List<String> messageList = new ArrayList<>();
            List<Object> dataList = new ArrayList<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                dataList.add(fieldError.getRejectedValue());
                messageList.add(fieldError.getDefaultMessage());
            }

//          오류 데이터를 다시 보낼 필요가 없으므로 success 로 유도할 필요 없으므로 BAD_REQUEST
            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    messageList,
                    dataList
            ), HttpStatus.BAD_REQUEST);
        }

        return null;
    }
}
