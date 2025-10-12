package com.example.bookreview.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractApiException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}