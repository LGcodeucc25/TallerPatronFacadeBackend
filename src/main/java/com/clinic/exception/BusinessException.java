package com.clinic.exception;

import org.springframework.http.HttpStatus;

/**
 * Business rule violation carrying the HTTP status that must be returned.
 */
public class BusinessException extends RuntimeException {

    private final HttpStatus status;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
