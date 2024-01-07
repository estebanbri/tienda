package com.example.tiendabackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaymentIntentDTO {
    private String description;
    private CartDTO cartDTO;
    private String currency;
    private String customer;

    public List<CartItemDTO> getCartItemsDTOs() {
        return this.cartDTO.getCartItemDTOs();
    }
}
