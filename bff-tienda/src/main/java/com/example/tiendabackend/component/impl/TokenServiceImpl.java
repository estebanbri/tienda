package com.example.tiendabackend.component.impl;


import com.example.tiendabackend.component.JwtService;
import com.example.tiendabackend.component.TokenService;
import com.example.tiendabackend.exception.InvalidTokenException;
import com.example.tiendabackend.exception.TokenValidQuantityExceededException;
import com.example.tiendabackend.entities.Token;
import com.example.tiendabackend.entities.AppUser;
import com.example.tiendabackend.repository.TokenRepository;
import com.example.tiendabackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenServiceImpl implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String TOKEN_QUANTITY_FIND_MESSAGE_ERROR = "More than one valid token found for the same tokentype:%s for user_id:%s";
    private static final String TOKEN_QUANTITY_SAVE_MESSAGE_ERROR = "Trying to save a new token without revoking last valid token tokentype:%s for user_id:%s";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Value("${token.generator.charset}")
    private String charSet;

    @Value("${token.generator.length}")
    private int tokenLength;

    private final Random random = new Random();

    @Override
    public Token save(Token newToken) {
        this.revokeAllUserTokensByTokenType(newToken.getUser(), newToken.getTokenType());
        var saveToken = this.tokenRepository.save(newToken);
        log.info("Created new token token_id:{} (type={}) stored for user_id: {}", saveToken.getId(), saveToken.getTokenType(), saveToken.getUserId());
        return saveToken;
    }

    @Override
    public List<Token> saveAll(List<Token> tokens) {
        if(!tokens.isEmpty()) this.revokeAllUserTokens(tokens.get(0).getUser());
        var saveTokens = this.tokenRepository.saveAll(tokens);
        tokens.forEach(token -> log.info("Created new token token_id:{} (type={}) stored for user_id: {}", token.getId(), token.getTokenType(), token.getUserId()));
        return saveTokens;
    }

    @Override
    public List<Token> findValidTokensByUserAndTokenType(AppUser user, Token.TokenType tokenType) {
        return this.tokenRepository.findAllValidTokensByUserIdAndTokenType(user.getId(), tokenType);
    }

    @Override
    public List<Token> findValidTokensByUser(AppUser user) {
        return this.tokenRepository.findAllValidTokensByUserId(user.getId());
    }

    @Override
    public Token findTokenByUserAndTokenType(AppUser user, Token.TokenType tokenType) {
       var tokens = this.findValidTokensByUserAndTokenType(user, tokenType);
       if(tokens.size() > 1) throw new TokenValidQuantityExceededException(String.format(TOKEN_QUANTITY_FIND_MESSAGE_ERROR, tokenType.name(), user.getId()));
       return tokens.stream()
               .findFirst()
               .orElse(null);
    }

    @Override
    public void revokeAllUserTokens(AppUser user) {
        log.info("Starting revokeAllUserTokens process for token and user_id: {}", user.getId());
        var tokens = this.findValidTokensByUser(user);
        revokeAll(tokens);
    }

    @Override
    public void revokeAllUserTokensByTokenType(AppUser user, Token.TokenType tokenType) {
        log.info("Starting revokeAllUserTokensByTokenType process for token (type={}) and user_id: {}", tokenType, user.getId());
        var tokens = this.findValidTokensByUserAndTokenType(user, tokenType);
        revokeAll(tokens);
    }

    @Override
    public String generateRandomToken() {
        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = 0; i < tokenLength; i++ ) {
            int digit = random.nextInt(tokenLength);
            tokenBuilder.append(digit);
        }
        var tokenAsString = tokenBuilder.toString();
        log.info("Success generation of random token");
        return tokenAsString;
    }

    @Override
    public boolean compare(String token1, String token2) {
        var same = token1.equals(token2);
        if(!same) {
            throw new InvalidTokenException("Token compare failed");
        }
        return true;
    }

    @Override
    public boolean isValid(String token) {
        return this.tokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
    }

    private void revokeAll(List<Token> tokens) {
        if(tokens.isEmpty()) { return; }
        for(Token token: tokens) {
            token.setExpired(true);
            token.setRevoked(true);
            token.setModifiedDate(new Date());
        }
        this.tokenRepository.saveAll(tokens);
        log.info("Success revoking process of previous tokens (quantity= {})", tokens.size());
    }
}
