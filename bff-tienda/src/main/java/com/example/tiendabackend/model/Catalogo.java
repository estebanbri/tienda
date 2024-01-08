package com.example.tiendabackend.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalogo {
    private List<CatalogoItem> catalogo;
}
