package com.example.tiendabackend.dto;

import lombok.Data;

@Data
public class ConfigDTO {
    private String stripeKey;
    private String recaptchaKey;
    private String gtmId;
}
