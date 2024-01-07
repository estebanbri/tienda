package com.example.tiendabackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PasswordChangeConfirmDTO {

    @NotNull
    private String subject;

    @NotNull
    private String password;

    @NotNull
    private String token;
}
