package com.example.tiendabackend.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "google.recaptcha")
public class CaptchaProperties {

    private String secret;
    private String url;
    private double scoreThreshold;
}