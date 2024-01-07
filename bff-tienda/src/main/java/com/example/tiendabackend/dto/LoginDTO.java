package com.example.tiendabackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDTO {
    @NotBlank
    private String email;
    @NotNull
    private String password;
}
