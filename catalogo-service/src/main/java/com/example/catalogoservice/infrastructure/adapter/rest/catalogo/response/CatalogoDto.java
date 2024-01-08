package com.example.catalogoservice.infrastructure.adapter.rest.catalogo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogoDto {

    @JsonProperty("catalogo")
    private List<CatalogoItemDto> catalogoItemDtos;

}
