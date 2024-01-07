package com.example.tiendabackend.model;

import lombok.Data;

@Data
public class CatalogoItem {
    private Long id;
    private String imageUrl;
    private String name;
    private String vendor;
    private long price;

    public CatalogoItem(Long id, String imageUrl, String name, String vendor, int price) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.vendor = vendor;
        this.price = price;
    }
}
