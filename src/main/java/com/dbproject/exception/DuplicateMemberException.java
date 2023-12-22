package com.dbproject.exception;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.PushBuilder;


public class DuplicateMemberException extends Exception {

    public DuplicateMemberException(String message){
        super(message);
    }

}
