package com.example.tiendabackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RecaptchaResponse {

    private boolean success;

    private String challengeTs;

    private String action;

    private String hostname;

    private double score;

    private List<String> errorCodes;

}