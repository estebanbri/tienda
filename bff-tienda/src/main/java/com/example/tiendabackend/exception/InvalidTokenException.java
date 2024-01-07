package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException  extends ApiException {

    private static final String MESSAGE_PREFIX = "Invalid token | ";

    public InvalidTokenException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.BAD_REQUEST);
    }
}
