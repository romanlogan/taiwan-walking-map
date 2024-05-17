package com.dbproject.exception;

public class CommentNotExistException extends RuntimeException {

    public CommentNotExistException(String message){
        super(message);
    }
}
