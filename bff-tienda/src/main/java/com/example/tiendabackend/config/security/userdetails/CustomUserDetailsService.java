package com.example.tiendabackend.config.security.userdetails;

import com.example.tiendabackend.exception.UserNotFoundException;
import com.example.tiendabackend.entities.AppUser;
import com.example.tiendabackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)  {
        AppUser user = userRepository.findByUsernameOrEmailAndAccountActivated(username)
            .orElseThrow(UserNotFoundException::new);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()) // sets email as username
                .password(user.getPassword())
                .roles(AppUser.Role.USER.name())
                .build();
    }
}