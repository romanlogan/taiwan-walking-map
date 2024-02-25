package com.dbproject.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

//    공통 처리시 어떻게 다중 에러를 처리하면서 에러가 생긴 데이터를 다시 보낼까
@Getter
public class ApiResponse<T> {

    private int code;   //status code 值
    private HttpStatus status;
    private List<String> messageList;     //error message
    private List<T> dataList;     //成功時，成功的data

    public ApiResponse(HttpStatus status, List<String> messageList, List<T> dataList) {
        this.code = status.value();
        this.status = status;
        this.messageList = messageList;
        this.dataList = dataList;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, List<String> messageList, List<T> dataList) {

        return new ApiResponse<>(status, messageList, dataList);
    }

//    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
//
//        return new ApiResponse<>(status, status.name(), data);
//    }

//    public static <T> ApiResponse<T> ok(T data) {
//
//        return new ApiResponse<>( HttpStatus.OK, HttpStatus.OK.name(), data);
//    }
}
