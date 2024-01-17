package com.dbproject.exception;

public class DuplicateFriendRequestException extends RuntimeException {

    public DuplicateFriendRequestException(String message){
        super(message);
    }
}
