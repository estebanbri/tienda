package com.example.tiendabackend.exception;


import org.springframework.http.HttpStatus;

public class JwtAccessTokenRequiredException extends ApiException {

    private static final String MESSAGE_PREFIX = "Jwt access token is missing | ";

    public JwtAccessTokenRequiredException(String message) {
        super(MESSAGE_PREFIX + message, HttpStatus.UNAUTHORIZED);
    }

}
