package com.gov.docmanagement.exception;

public class DepartmentAlreadyExistException extends RuntimeException{
    public DepartmentAlreadyExistException(String msg){
        super(msg);
    }

}
