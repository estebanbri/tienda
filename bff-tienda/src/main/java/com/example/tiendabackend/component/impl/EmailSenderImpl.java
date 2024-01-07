package com.example.tiendabackend.component.impl;

import com.example.tiendabackend.component.EmailSender;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSenderImpl implements EmailSender {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${email.from}")
    private String emailFrom;

    @Override
    @SneakyThrows
    public void sendSimpleMessage(String to, String subject, String text)  {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        // messageHelper.setFrom(); // No es necesario setearlo porque toma el valor de la propiedad de spring.mail.username under the hood
        messageHelper.setReplyTo(new InternetAddress(this.emailFrom));
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(text, true);
        emailSender.send(mimeMessage);
    }

}
