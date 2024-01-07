package com.example.tiendabackend.component;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tiendabackend.entities.AppUser;
import com.example.tiendabackend.enums.JwtScope;

public interface JwtService {
    String generateAccessToken(AppUser user);
    String generateRefreshToken(AppUser user);
    DecodedJWT verifyAndDecodeJwt(String jwt, JwtScope targetScope) throws TokenExpiredException;
    DecodedJWT decodeJwt(String jwt);
}
