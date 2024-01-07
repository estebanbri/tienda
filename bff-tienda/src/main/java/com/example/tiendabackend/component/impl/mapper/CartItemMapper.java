package com.example.tiendabackend.component.impl.mapper;

import com.example.tiendabackend.dto.CartItemDTO;
import com.example.tiendabackend.model.CartItem;
import com.example.tiendabackend.model.CatalogoItem;
import com.example.tiendabackend.service.CatalogoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartItemMapper {

    @Autowired
    private CatalogoService catalogoService;

    public List<CartItem> mapToCartItems(List<CartItemDTO> cartItemsDTOs) {
        var catalogoItems = catalogoService.getAll();
        return cartItemsDTOs.stream()
                .map(cartItemDTO -> {
                    CartItem cartItem = new CartItem();
                    BeanUtils.copyProperties(cartItemDTO, cartItem);
                    cartItem.setPrice(getPriceByCatalogoItemId(cartItem.getCatalogoItemId(), catalogoItems));
                    return cartItem;
                }).collect(Collectors.toList());
    }

    private long getPriceByCatalogoItemId(Long catalogoItemId, List<CatalogoItem> catalogoItems) {
        return catalogoItems.stream()
                .filter(catalogoItem -> catalogoItem.getId().equals(catalogoItemId))
                .map(CatalogoItem::getPrice).findFirst()
                .orElseThrow(() -> new RuntimeException("Price not found"));
    }
}
