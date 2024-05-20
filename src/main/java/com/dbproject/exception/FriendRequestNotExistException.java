package com.dbproject.exception;

public class FriendRequestNotExistException extends RuntimeException {

    public FriendRequestNotExistException(String message){
        super(message);
    }
}
