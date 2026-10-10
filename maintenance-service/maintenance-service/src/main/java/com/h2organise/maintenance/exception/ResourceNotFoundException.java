package com.h2organise.maintenance.exception;

// Thrown when a record with the given id does not exist. Turned into HTTP 404.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
