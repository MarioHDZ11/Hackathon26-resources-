package com.BiSourceMX.Proyect.repository;

import com.BiSourceMX.Proyect.model.DistribucionRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistribucionRecursoRepository extends JpaRepository<DistribucionRecurso, Integer> {

    List<DistribucionRecurso> findByEstadoIdEstado(Integer idEstado);

    List<DistribucionRecurso> findByRecursoIdRecurso(Integer idRecurso);
}
