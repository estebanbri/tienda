package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class TokenInvalidScopeException extends ApiException{

    private static final String MESSAGE_PREFIX = "Token with invalid scope | ";

    public TokenInvalidScopeException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.UNAUTHORIZED);
    }
}

