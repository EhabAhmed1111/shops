package com.e_commerceapp.clothshops.exceptionhandler;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
