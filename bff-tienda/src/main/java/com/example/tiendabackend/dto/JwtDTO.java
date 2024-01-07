package com.example.tiendabackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDTO {
    // Valor del token
    private String token;
}
