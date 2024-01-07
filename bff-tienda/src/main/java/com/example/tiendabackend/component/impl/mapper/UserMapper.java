package com.example.tiendabackend.component.impl.mapper;

import com.example.tiendabackend.dto.UserDTO;
import com.example.tiendabackend.entities.AppUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(AppUser user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRole(user.getRole().name());
        return userDTO;
    }
}
