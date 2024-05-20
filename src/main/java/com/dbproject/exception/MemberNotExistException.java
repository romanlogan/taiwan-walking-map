package com.dbproject.exception;

public class MemberNotExistException extends RuntimeException {

    public MemberNotExistException(String message){
        super(message);
    }

}
