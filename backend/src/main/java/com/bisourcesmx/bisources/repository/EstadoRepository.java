package com.bisourcesmx.bisources.repository;

import com.bisourcesmx.bisources.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    Optional<Estado> findByNombre(String nombre);
    Optional<Estado> findByCodigo3(String codigo3);
}
