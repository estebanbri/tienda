package com.example.tiendabackend.service.impl;


import com.example.tiendabackend.dto.UpdateUserDTO;
import com.example.tiendabackend.exception.AuthenticationException;
import com.example.tiendabackend.exception.UserNotFoundException;
import com.example.tiendabackend.entities.AppUser;
import com.example.tiendabackend.repository.UserRepository;
import com.example.tiendabackend.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public AppUser getCurrentLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Verificar si el usuario está autenticado
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException("User is not authorized");
        }
        // Obtener detalles específicos del usuario
        Object principal = authentication.getPrincipal();

        return this.userRepository.findByUsernameOrEmailAndAccountActivated(String.valueOf(principal))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AppUser save(AppUser user) {
        return this.userRepository.save(user);
    }

    @Override
    public AppUser findByUsernameOrEmailAccountActivated(String value) {
        return this.userRepository.findByUsernameOrEmailAndAccountActivated(value)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AppUser findByUsernameOrEmail(String value) {
        return this.userRepository.findByUsernameOrEmail(value)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AppUser findById(Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AppUser updateUser(Long userId, UpdateUserDTO userDTO) {
        AppUser user = this.findById(userId);
        BeanUtils.copyProperties(userDTO, user);
        var savedUser = this.userRepository.save(user);
        log.info("Update success for user_id {}", user.getId());
        return savedUser;
    }

}
