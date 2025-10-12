package com.example.bookreview.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends AbstractApiException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
