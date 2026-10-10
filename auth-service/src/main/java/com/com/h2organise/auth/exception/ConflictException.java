package com.h2organise.auth.exception;

// e.g. the email is already registered -> 409
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
