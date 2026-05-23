package com.BiSourceMX.Proyect.repository;

import com.BiSourceMX.Proyect.model.EstadoPartida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadoPartidaRepository extends JpaRepository<EstadoPartida, Integer> {

    List<EstadoPartida> findByPartidaIdPartida(Integer idPartida);

    void deleteByPartidaIdPartida(Integer idPartida);
}
