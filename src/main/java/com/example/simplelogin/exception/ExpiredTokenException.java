package com.example.simplelogin.exception;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String message) {
        super("Expired token: " + message);
    }
}
