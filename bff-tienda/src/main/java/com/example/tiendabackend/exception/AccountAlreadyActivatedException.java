package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class AccountAlreadyActivatedException extends ApiException {

    private static final String MESSAGE_PREFIX = "Account already activated | ";

    public AccountAlreadyActivatedException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.BAD_REQUEST);
    }
}
