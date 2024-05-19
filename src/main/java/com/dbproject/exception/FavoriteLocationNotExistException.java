package com.dbproject.exception;

public class FavoriteLocationNotExistException extends RuntimeException {

    public FavoriteLocationNotExistException(String message){
        super(message);
    }
}
