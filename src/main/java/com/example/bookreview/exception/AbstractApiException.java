package com.example.bookreview.exception;

import org.springframework.http.HttpStatus;

class AbstractApiException extends RuntimeException {

    private final HttpStatus status;

    protected AbstractApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return  status;
    }
}
