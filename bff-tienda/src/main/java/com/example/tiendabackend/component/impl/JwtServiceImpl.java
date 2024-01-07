package com.example.tiendabackend.component.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tiendabackend.component.JwtService;
import com.example.tiendabackend.enums.JwtScope;
import com.example.tiendabackend.exception.TokenInvalidScopeException;
import com.example.tiendabackend.exception.TokenRequired;
import com.example.tiendabackend.entities.AppUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtServiceImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_token.expiration.ms}")
    private int jwtAccessTokenExpirationMs;

    @Value("${jwt.refresh_token.expiration.ms}")
    private int jwtRefreshTokenExpirationMs;

    @Override
    public String generateAccessToken(AppUser user) {
        log.info("Generation new access_token for user_id: {}", user.getId());
        return generateToken(user, this.jwtAccessTokenExpirationMs, JwtScope.READ_WRITE);
    }

    @Override
    public String generateRefreshToken(AppUser user) {
        log.info("Generation new refresh_token for user_id: {}", user.getId());
        return generateToken(user, this.jwtRefreshTokenExpirationMs, JwtScope.REFRESH_TOKEN);
    }

    @Override
    public DecodedJWT verifyAndDecodeJwt(String jwt, JwtScope jwtScope) throws TokenExpiredException {
        JWTVerifier jwtVerifier = JWT.require(algorithm()).build();
        DecodedJWT decodedJWT = null;
        try{
            decodedJWT = jwtVerifier.verify(jwt);
            this.validateJwtScope(decodedJWT, jwtScope);
        } catch (TokenExpiredException e) {
            log.error(e.getMessage());
            throw e;
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            throw new TokenRequired();
        }
        return decodedJWT;
    }

    @Override
    public DecodedJWT decodeJwt(String jwt) {
        return JWT.decode(jwt);
    }

    private String generateToken(AppUser user, long expirationMs, JwtScope scope) {
        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("scope", scope.getValue())
                .sign(algorithm);
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256(this.secret);
    }

    private void validateJwtScope(DecodedJWT decodedJWT, JwtScope targetScope) {
        String tokenScope = decodedJWT.getClaim("scope").asString();
        if(!targetScope.getValue().equals(tokenScope)) {
            throw new TokenInvalidScopeException(String.format("%s scope needed but found %s ", targetScope, tokenScope));
        }
    }
}
