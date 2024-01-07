package com.example.tiendabackend.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String country;
}
