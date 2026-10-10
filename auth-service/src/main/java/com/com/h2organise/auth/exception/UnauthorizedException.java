package com.h2organise.auth.exception;

// wrong email or password, or a deactivated account -> 401
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
