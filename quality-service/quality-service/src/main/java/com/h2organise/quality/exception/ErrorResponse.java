package com.h2organise.quality.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;

// The error body every H2Organise service returns: { "timestamp", "status", "message" }
@Getter
@JsonPropertyOrder({ "timestamp", "status", "message" })
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String message;

    public ErrorResponse(int status, String message) {
        this.timestamp = LocalDateTime.now().withNano(0);
        this.status = status;
        this.message = message;
    }
}
