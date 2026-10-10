package com.h2organise.complaint.exception;

import java.time.LocalDateTime;

// the error format the whole team agreed on: { timestamp, status, message }
public record ApiError(LocalDateTime timestamp, int status, String message) {

    public static ApiError of(int status, String message) {
        return new ApiError(LocalDateTime.now(), status, message);
    }
}
