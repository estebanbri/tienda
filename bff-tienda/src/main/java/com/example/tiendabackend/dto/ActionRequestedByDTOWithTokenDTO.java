package com.example.tiendabackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ActionRequestedByDTOWithTokenDTO extends ActionRequestedByDTO {
    @NotNull
    private String token;
}
