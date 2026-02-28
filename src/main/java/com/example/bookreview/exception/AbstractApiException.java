package com.example.bookreview.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
class AbstractApiException extends RuntimeException {

    private final HttpStatus status;

    protected AbstractApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
