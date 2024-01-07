package com.example.tiendabackend.component.impl;

import com.example.tiendabackend.component.ConfigService;
import com.example.tiendabackend.component.impl.mapper.ConfigMapper;
import com.example.tiendabackend.dto.ConfigDTO;
import com.example.tiendabackend.entities.Config;
import com.example.tiendabackend.exception.ConfigLoadException;
import com.example.tiendabackend.repository.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {

    private static final Logger log = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public ConfigDTO getConfig() {
        Config config = configRepository.findAll().stream()
                .findFirst()
                .orElseThrow(ConfigLoadException::new);
        log.info("Success requesting app config");
        return configMapper.toDTO(config);
    }

}
