package com.example.tiendabackend.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
public class AppUser implements UserDetails {

    public enum Role {
        ADMIN,
        USER
    }

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String country;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, unique = true)
    private String email;
    private boolean accountActivated;
    private boolean passwordResetRequested;

//    @OneToMany(mappedBy = "user")
//    private List<Token> tokens = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//    public void addToken(Token token) {
//        this.tokens.add(token);
//    }

}
