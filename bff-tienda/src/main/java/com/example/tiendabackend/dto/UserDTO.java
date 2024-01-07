package com.example.tiendabackend.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String country;
    @NotBlank
    private String role;
    @Email
    private String email;
    private boolean accountActivated;
}
