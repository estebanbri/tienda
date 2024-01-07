package com.example.tiendabackend.component;

import com.example.tiendabackend.dto.RecaptchaResponse;

public interface CaptchaService {
    RecaptchaResponse verify(String tokenResponse);
}
