package com.h2organise.complaint.exception;

// thrown when we look for something by id and it's not in the db
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
