package com.h2organise.quality.exception;

/** Thrown when a request is well-formed JSON but breaks a business rule. Mapped to HTTP 400. */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
