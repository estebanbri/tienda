package com.example.tiendabackend.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long catalogoItemId;
    private int quantity;
}
