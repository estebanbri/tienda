package com.example.tiendabackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtPairDTO {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("refresh_token")
    String refreshToken;
}
