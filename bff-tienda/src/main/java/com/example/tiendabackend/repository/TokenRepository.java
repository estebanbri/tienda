package com.example.tiendabackend.repository;

import com.example.tiendabackend.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t inner join AppUser u on t.user.id = u.id " +
            "where u.id = :userId " +
            "and (t.expired = false and t.revoked = false) " +
            "and t.tokenType = :tokenType")
    List<Token> findAllValidTokensByUserIdAndTokenType(Long userId, Token.TokenType tokenType);

    @Query("select t from Token t inner join AppUser u on t.user.id = u.id " +
            "where u.id = :userId " +
            "and (t.expired = false and t.revoked = false) ")
    List<Token> findAllValidTokensByUserId(Long userId);

    @Query("select t from Token t inner join AppUser u on t.user.id = u.id " +
            "where u.username = :username " +
            "and (t.expired = false and t.revoked = false) ")
    List<Token> findAllValidTokensByUsername(String username);

    Optional<Token> findByToken(String token);


}
