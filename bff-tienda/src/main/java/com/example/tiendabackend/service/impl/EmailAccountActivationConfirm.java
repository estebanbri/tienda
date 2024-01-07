package com.example.tiendabackend.service.impl;


import com.example.tiendabackend.component.EmailSender;
import com.example.tiendabackend.entities.AppUser;
import com.example.tiendabackend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Service("accountActivationEmailSender")
public class EmailAccountActivationConfirm implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailAccountActivationConfirm.class);

    @Value("${email.subject.account.activation:subject}")
    private String emailSubjectActivacion;

    @Value("${email.body.account.activation:body}")
    private String emailBodyActivacion;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void send(AppUser user, String token) {
        CompletableFuture.runAsync(() -> {
            Objects.requireNonNull(user, "User can not be null");
            log.info("Sending activation client email to user {}", user.getUsername());
            this.emailSender.sendSimpleMessage(
                    user.getEmail(),
                    this.emailSubjectActivacion,
                    this.generateAccountActivationEmailBodyWithActivationLink(token)
            );
        });
    }

    private String generateAccountActivationEmailBodyWithActivationLink(String token) {
        return this.emailBodyActivacion.replace("{{code}}", token);
    }

}
