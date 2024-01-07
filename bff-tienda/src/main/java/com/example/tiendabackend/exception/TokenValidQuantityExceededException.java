package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class TokenValidQuantityExceededException  extends ApiException {
    public TokenValidQuantityExceededException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
