package com.gov.docmanagement.exception;

public class WrongPasswordException extends RuntimeException{

    public WrongPasswordException(String msg){
        super(msg);
    }
}
