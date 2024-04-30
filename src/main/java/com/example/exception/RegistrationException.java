package com.example.exception;

public class RegistrationException extends RuntimeException{
    public RegistrationException(String msg) {
        super(msg);
    }
}
