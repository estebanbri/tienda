package com.example.tiendabackend.service.impl;

import com.example.tiendabackend.model.CatalogoItem;
import com.example.tiendabackend.service.CatalogoService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    @Override
    public List<CatalogoItem> getAll() {
        return Arrays.asList(
                new CatalogoItem(1L, "headphone.jpg","Auricular Monoaureal","Sony",14000),
                new CatalogoItem(2L, "keyboard.jpg","Teclado Mecanico","Raizer",55000),
                new CatalogoItem(3L, "keyboard.jpg","Teclado Inalambrico","Sony",74000),
                new CatalogoItem(4L, "headphone.jpg","Auricular Inalambricos","Sony",22000),
                new CatalogoItem(5L, "headphone.jpg","Auriculares 8.1","Phillips",44000),
                new CatalogoItem(6L, "keyboard.jpg","Teclado Gamer","Logitech",11000),
                new CatalogoItem(7L, "monitor.jpg","Monitor 24 pulgadas","Samsung",99000)
        );
    }
}
