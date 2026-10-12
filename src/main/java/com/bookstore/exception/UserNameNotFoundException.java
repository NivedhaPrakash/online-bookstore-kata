package com.bookstore.exception;

public class UserNameNotFoundException extends RuntimeException{

    public UserNameNotFoundException(String message) {
        super(message);
    }
}
