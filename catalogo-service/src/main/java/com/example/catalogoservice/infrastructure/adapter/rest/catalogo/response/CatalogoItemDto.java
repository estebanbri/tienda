package com.example.catalogoservice.infrastructure.adapter.rest.catalogo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoItemDto {
    private Long id;
    private String imageUrl;
    private String name;
    private String vendor;
    private long price;
}
