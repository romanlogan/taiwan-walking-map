package com.dbproject.exception;

public class FavoriteCountLessThanOrEqualZeroException extends RuntimeException {

    public FavoriteCountLessThanOrEqualZeroException(String message){
        super(message);
    }
}
