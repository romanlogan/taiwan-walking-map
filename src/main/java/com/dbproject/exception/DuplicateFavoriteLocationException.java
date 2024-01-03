package com.dbproject.exception;

public class DuplicateFavoriteLocationException extends RuntimeException{

    public DuplicateFavoriteLocationException(String message){
        super(message);
    }

}
