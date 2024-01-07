package com.example.tiendabackend.enums;

public enum JwtScope {
    READ_WRITE("read write"),
    REFRESH_TOKEN("refresh_token");

    private final String value;

    JwtScope(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
