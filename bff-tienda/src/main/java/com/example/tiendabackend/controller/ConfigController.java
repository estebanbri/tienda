package com.example.tiendabackend.controller;

import com.example.tiendabackend.component.ConfigService;
import com.example.tiendabackend.controller.endpoints.ApiConstants;
import com.example.tiendabackend.dto.ConfigDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value= ApiConstants.V1)
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping(ApiConstants.CONFIG_BASE)
    public ConfigDTO getConfig() {
        return this.configService.getConfig();
    }

}
