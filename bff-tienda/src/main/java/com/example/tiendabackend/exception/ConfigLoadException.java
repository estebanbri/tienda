package com.example.tiendabackend.exception;

import org.springframework.http.HttpStatus;

public class ConfigLoadException extends ApiException {

    private static final String MESSAGE = "Unable to find config...";

    public ConfigLoadException() {
        super(MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
