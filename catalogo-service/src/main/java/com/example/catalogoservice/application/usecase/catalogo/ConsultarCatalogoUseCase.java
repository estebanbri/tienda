package com.example.catalogoservice.application.usecase.catalogo;

import com.example.catalogoservice.application.common.UseCase;
import com.example.catalogoservice.application.port.catalogo.CatalogoRepositorioPort;
import com.example.catalogoservice.domain.model.Catalogo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class ConsultarCatalogoUseCase {

    private final CatalogoRepositorioPort catalogoRepositorio;

    public Catalogo execute() {
        log.info("Obteniendo catalogo");
        return catalogoRepositorio.obtenerCatalogo();
    }
}
