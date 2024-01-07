package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class TokenRequired extends ApiException{

    private static final String MESSAGE_PREFIX = "Token required";

    public TokenRequired() {
        super(MESSAGE_PREFIX, HttpStatus.BAD_REQUEST);
    }
}
