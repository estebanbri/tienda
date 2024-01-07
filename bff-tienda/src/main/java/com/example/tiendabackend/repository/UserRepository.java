package com.example.tiendabackend.repository;

import com.example.tiendabackend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);

    @Query("from AppUser u where (u.username like :value or u.email like :value) and u.accountActivated = TRUE")
    Optional<AppUser> findByUsernameOrEmailAndAccountActivated(String value);

    @Query("from AppUser u where (u.username like :value or u.email like :value)")
    Optional<AppUser> findByUsernameOrEmail(String value);

    @Query("select case when count(u)> 0 then true else false end from AppUser u where u.email like :email")
    boolean existsByEmail(@Param("email") String email);


}
