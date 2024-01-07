package com.example.tiendabackend.component;

import com.example.tiendabackend.entities.Token;
import com.example.tiendabackend.entities.AppUser;

import java.util.List;


public interface TokenService {
    Token save(Token token);
    List<Token> saveAll(List<Token> tokens);
    List<Token> findValidTokensByUserAndTokenType(AppUser user, Token.TokenType tokenType);
    List<Token> findValidTokensByUser(AppUser user);
    Token findTokenByUserAndTokenType(AppUser user, Token.TokenType tokenType);
    void revokeAllUserTokens(AppUser user);
    void revokeAllUserTokensByTokenType(AppUser user, Token.TokenType tokenType);
    String generateRandomToken();
    boolean compare(String token1, String token2);
    boolean isValid(String jwt);
}
