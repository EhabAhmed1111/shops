package com.e_commerceapp.clothshops.exceptionhandler;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
