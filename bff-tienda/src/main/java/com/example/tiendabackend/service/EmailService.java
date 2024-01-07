package com.example.tiendabackend.service;


import com.example.tiendabackend.entities.AppUser;

public interface EmailService {
    void send(AppUser user, String token);
}
