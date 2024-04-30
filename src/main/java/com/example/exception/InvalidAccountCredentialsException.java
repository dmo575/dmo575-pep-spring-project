package com.example.exception;

public class InvalidAccountCredentialsException extends RuntimeException {
    public InvalidAccountCredentialsException(String msg) {
        super(msg);
    }
}
