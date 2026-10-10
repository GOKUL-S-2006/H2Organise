package com.h2organise.maintenance.exception;

import java.time.LocalDateTime;

// error format agreed by the team: { timestamp, status, message }
public record ErrorResponse(LocalDateTime timestamp, int status, String message) {

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(LocalDateTime.now(), status, message);
    }
}
