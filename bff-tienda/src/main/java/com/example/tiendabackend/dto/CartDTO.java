package com.example.tiendabackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private List<CartItemDTO> cartItemDTOs;
}
