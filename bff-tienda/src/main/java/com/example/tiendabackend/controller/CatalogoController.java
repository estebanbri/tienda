package com.example.tiendabackend.controller;

import com.example.tiendabackend.controller.endpoints.ApiConstants;
import com.example.tiendabackend.model.CatalogoItem;
import com.example.tiendabackend.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value= ApiConstants.V1)
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(summary = "Obtener todo el catalogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtencion lista de items de catalogo",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CatalogoItem.class)) })})
    @GetMapping(ApiConstants.CATALOGOS_BASE)
    public List<CatalogoItem> getAll() {
        return catalogoService.getAll();
    }

}
