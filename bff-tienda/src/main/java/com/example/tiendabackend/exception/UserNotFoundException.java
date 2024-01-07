package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException  extends ApiException {

    private static final String MESSAGE_PREFIX = "User not found or account activation is pending";
    public UserNotFoundException() {
        super(MESSAGE_PREFIX, HttpStatus.NOT_FOUND);
    }
}
