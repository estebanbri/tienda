package com.example.tiendabackend.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Config {

    @Id
    @GeneratedValue
    private Long id;

    private String stripeKey;
    private String recaptchaKey;
    private String gtmId;
}
