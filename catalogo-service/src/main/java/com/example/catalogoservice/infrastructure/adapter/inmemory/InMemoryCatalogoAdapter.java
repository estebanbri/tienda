package com.example.catalogoservice.infrastructure.adapter.inmemory;

import com.example.catalogoservice.application.port.catalogo.CatalogoRepositorioPort;
import com.example.catalogoservice.domain.model.Catalogo;
import com.example.catalogoservice.domain.model.CatalogoItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InMemoryCatalogoAdapter implements CatalogoRepositorioPort {

    @Override
    public Catalogo obtenerCatalogo() {
        return Catalogo.builder().catalogoItems(
                Arrays.asList(
                        new CatalogoItem(1L, "headphone.jpg", "Auricular Monoaureal", "Sony", 14000),
                        new CatalogoItem(2L, "keyboard.jpg", "Teclado Mecanico", "Raizer", 55000),
                        new CatalogoItem(3L, "keyboard.jpg", "Teclado Inalambrico", "Sony", 74000),
                        new CatalogoItem(4L, "headphone.jpg", "Auricular Inalambricos", "Sony", 22000),
                        new CatalogoItem(5L, "headphone.jpg", "Auriculares 8.1", "Phillips", 44000),
                        new CatalogoItem(6L, "keyboard.jpg", "Teclado Gamer", "Logitech", 11000),
                        new CatalogoItem(7L, "monitor.jpg", "Monitor 24 pulgadas", "Samsung", 99000)
                )
        ).build();
    }
}
