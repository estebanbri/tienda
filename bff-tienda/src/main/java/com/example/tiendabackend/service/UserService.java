package com.example.tiendabackend.service;

import com.example.tiendabackend.dto.UpdateUserDTO;
import com.example.tiendabackend.entities.AppUser;


public interface UserService {
    AppUser getCurrentLoggedUser();
    AppUser save(AppUser user);
    AppUser findByUsernameOrEmailAccountActivated(String value);
    AppUser findByUsernameOrEmail(String email);
    AppUser findById(Long userId);
    AppUser updateUser(Long userId, UpdateUserDTO userDTO);
}
