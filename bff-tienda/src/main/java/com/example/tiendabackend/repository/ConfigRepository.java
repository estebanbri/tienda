package com.example.tiendabackend.repository;

import com.example.tiendabackend.entities.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Long> {
}
