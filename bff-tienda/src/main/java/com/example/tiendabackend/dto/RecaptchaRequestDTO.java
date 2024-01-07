package com.example.tiendabackend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class RecaptchaRequestDTO {
    String secret;
    String response;
}
