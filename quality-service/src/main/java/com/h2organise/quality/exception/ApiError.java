package com.h2organise.quality.exception;

import java.time.LocalDateTime;

/** Error body shared by every H2Organise service: { "timestamp", "status", "message" }. */
public record ApiError(LocalDateTime timestamp, int status, String message) {
    public static ApiError of(int status, String message) {
        return new ApiError(LocalDateTime.now(), status, message);
    }
}
