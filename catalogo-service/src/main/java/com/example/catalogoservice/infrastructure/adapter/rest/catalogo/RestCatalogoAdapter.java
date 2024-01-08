package com.example.catalogoservice.infrastructure.adapter.rest.catalogo;

import com.example.catalogoservice.application.usecase.catalogo.ConsultarCatalogoUseCase;
import com.example.catalogoservice.infrastructure.adapter.rest.catalogo.mapper.CatalogoDtoMapper;
import com.example.catalogoservice.infrastructure.adapter.rest.catalogo.response.CatalogoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RestCatalogoAdapter {

    private final ConsultarCatalogoUseCase consultarCatalogoUseCase;

    private final CatalogoDtoMapper catalogoDtoMapper;

    // TODO implementar paginado cuando se almacene en db
    @GetMapping("/catalogo")
    public ResponseEntity<CatalogoDto> obtenerCatalogo() {
        return new ResponseEntity<>(catalogoDtoMapper.toDto(consultarCatalogoUseCase.execute()), HttpStatus.OK);
    }
}
