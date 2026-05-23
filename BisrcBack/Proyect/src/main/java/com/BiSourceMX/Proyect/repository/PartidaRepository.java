package com.BiSourceMX.Proyect.repository;

import com.BiSourceMX.Proyect.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Integer> {

    Optional<Partida> findFirstByActivaTrueOrderByFechaCreacionDesc();
}
