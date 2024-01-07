package com.example.tiendabackend.component.impl.mapper;

import com.example.tiendabackend.dto.ConfigDTO;
import com.example.tiendabackend.entities.Config;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ConfigMapper {

    public ConfigDTO toDTO(Config config) {
        ConfigDTO configDTO = new ConfigDTO();
        BeanUtils.copyProperties(config, configDTO);
        return configDTO;
    }

}
