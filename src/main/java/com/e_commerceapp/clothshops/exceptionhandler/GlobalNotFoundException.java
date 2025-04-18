package com.e_commerceapp.clothshops.exceptionhandler;


public class GlobalNotFoundException extends RuntimeException {
    public GlobalNotFoundException(String message) {
        super(message);
    }
}
