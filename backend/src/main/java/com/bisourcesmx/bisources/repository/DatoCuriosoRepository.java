package com.bisourcesmx.bisources.repository;

import com.bisourcesmx.bisources.model.DatoCurioso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatoCuriosoRepository extends JpaRepository<DatoCurioso, Long> {

    List<DatoCurioso> findByEstadoId(Integer estadoId);

    @Query(value = "SELECT d.dato_curioso FROM datos_curiosos d WHERE d.id_estado = :estadoId ORDER BY RAND() LIMIT 1", nativeQuery = true)
    String findRandomDatoByEstadoId(@Param("estadoId") Integer estadoId);
}
