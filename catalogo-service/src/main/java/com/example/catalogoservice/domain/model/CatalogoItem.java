package com.example.catalogoservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoItem {
    private Long id;
    private String imageUrl;
    private String name;
    private String vendor;
    private long price;
}
