package com.example.tiendabackend.service.impl;

import com.example.tiendabackend.model.Catalogo;
import com.example.tiendabackend.model.CatalogoItem;
import com.example.tiendabackend.service.CatalogoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<CatalogoItem> getAll() {
        var data = restTemplate.getForObject("http://catalogo-service:8080/api/v1/catalogo", Catalogo.class);
        return data.getCatalogo();
    }
}
