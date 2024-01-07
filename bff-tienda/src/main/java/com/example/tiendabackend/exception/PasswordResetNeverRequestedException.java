package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class PasswordResetNeverRequestedException  extends ApiException {

    private static final String MESSAGE_PREFIX = "Password reset never requested | ";

    public PasswordResetNeverRequestedException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.BAD_REQUEST);
    }
}
