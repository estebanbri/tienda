package com.example.tiendabackend.model;


import lombok.Data;

import java.util.List;

@Data
public class Cart {
    private Long id;
    private List<CartItem> cartItemLists;
    private long amount;
}
