package com.example.tiendabackend.component;

public interface EmailSender {
    void sendSimpleMessage(String to, String subject, String text);
}
