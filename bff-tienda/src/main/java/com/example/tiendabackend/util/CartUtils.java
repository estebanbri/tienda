package com.example.tiendabackend.util;

import com.example.tiendabackend.model.Cart;
import com.example.tiendabackend.model.CartItem;

public class CartUtils {

    public static Long calculateTotalAmount(Cart cart) {
        return cart.getCartItemLists().stream().mapToLong(CartItem::getPrice).sum();
    }
}
