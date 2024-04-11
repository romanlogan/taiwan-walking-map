package com.dbproject.exception;

public class CommentCountLessThanOrEqualZeroException extends RuntimeException {

    public CommentCountLessThanOrEqualZeroException(String message){
        super(message);
    }
}
