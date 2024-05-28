package com.dbproject.exception;

public class LocationNotExistException extends RuntimeException {

    public LocationNotExistException(String message){
        super(message);
    }
}
