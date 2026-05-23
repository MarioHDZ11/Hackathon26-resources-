package com.BiSourceMX.Proyect.repository;

import com.BiSourceMX.Proyect.model.DatoCurioso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatoCuriosoRepository extends JpaRepository<DatoCurioso, Integer> {

    List<DatoCurioso> findByEstadoIdEstado(Integer idEstado);
}
