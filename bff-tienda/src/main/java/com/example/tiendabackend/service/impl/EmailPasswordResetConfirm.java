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

@Service("emailSenderPasswordResetConfirm")
public class EmailPasswordResetConfirm implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailPasswordResetConfirm.class);

    @Value("${email.subject.password.reset:subject}")
    private String emailSubjectPasswordReset;

    @Value("${email.body.password.reset:body}")
    private String emailBodyPasswordReset;

    @Autowired
    private EmailSender emailSender;

    @Override
    public void send(AppUser user, String token) {
        CompletableFuture.runAsync(() -> {
            Objects.requireNonNull(user, "User can not be null");
            log.info("Sending password reset request email to user {}", user.getUsername());
            this.emailSender.sendSimpleMessage(
                    user.getEmail(),
                    this.emailSubjectPasswordReset,
                    this.generatePasswordResetEmailBodyWithActivationLink(token)
            );
        });
    }

    private String generatePasswordResetEmailBodyWithActivationLink(String token) {
        return this.emailBodyPasswordReset.replace("{{code}}", token);
    }
}
