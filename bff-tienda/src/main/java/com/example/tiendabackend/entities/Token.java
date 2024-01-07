package com.example.tiendabackend.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static java.util.Optional.ofNullable;

@Entity
@Data
public class Token {

    public enum TokenType {
        ACCESS_TOKEN,
        REFRESH_TOKEN,
        PASSWORD_RESET,
        ACCOUNT_ACTIVATION
    }

    @Id
    @GeneratedValue
    private Long id;

    private Date createdDate;

    private Date modifiedDate;

    private boolean expired;

    private boolean revoked;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    public Token(String token, TokenType tokenType, AppUser user) {
        this();
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
    }

    public Token() {
        this.createdDate = new Date();
    }

    public Long getUserId() {
        return ofNullable(user)
                .map(AppUser::getId)
                .orElse(null);
    }
}
