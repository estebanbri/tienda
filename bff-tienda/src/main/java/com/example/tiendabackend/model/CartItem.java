package com.example.tiendabackend.model;

import lombok.Data;

@Data
public class CartItem {
    private Long id;
    private Long catalogoItemId;
    private int quantity;
    private long price;
}
