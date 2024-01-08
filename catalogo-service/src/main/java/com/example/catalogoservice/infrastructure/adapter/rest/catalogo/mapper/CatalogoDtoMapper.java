package com.example.catalogoservice.infrastructure.adapter.rest.catalogo.mapper;

import com.example.catalogoservice.domain.model.Catalogo;
import com.example.catalogoservice.infrastructure.adapter.rest.catalogo.response.CatalogoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CatalogoDtoMapper {

    @Mapping(target = "catalogoItemDtos", source = "catalogoItems")
    CatalogoDto toDto(Catalogo catalogo);

}
