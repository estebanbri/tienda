package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class PasswordResetAlreadyRequestedException  extends ApiException {

    private static final String MESSAGE_PREFIX = "Password reset already requested | ";

    public PasswordResetAlreadyRequestedException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.BAD_REQUEST);
    }
}
