package com.example.tiendabackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ActionRequestedByDTO {
    @NotNull
    private String subject;
}
