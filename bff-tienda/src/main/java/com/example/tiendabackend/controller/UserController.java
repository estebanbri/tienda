package com.example.tiendabackend.controller;

import com.example.tiendabackend.controller.endpoints.ApiConstants;
import com.example.tiendabackend.dto.UpdateUserDTO;
import com.example.tiendabackend.dto.UserDTO;
import com.example.tiendabackend.component.impl.mapper.UserMapper;
import com.example.tiendabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiConstants.V1)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper mapper;

    @GetMapping(ApiConstants.USERS_ME)
    public ResponseEntity<UserDTO> getCurrentLoggedUser() {
        return ResponseEntity.ok(mapper.toDTO(userService.getCurrentLoggedUser()));
    }

    @PutMapping(ApiConstants.USER_UPDATE)
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO userDTO) {
        return ResponseEntity.ok(mapper.toDTO(userService.updateUser(id, userDTO)));
    }

}
