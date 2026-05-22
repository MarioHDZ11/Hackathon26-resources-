package com.BiSourceMX.Proyect.repository;

import com.BiSourceMX.Proyect.model.RecursoEstado;
import com.BiSourceMX.Proyect.model.RecursoEstadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoEstadoRepository extends JpaRepository<RecursoEstado, RecursoEstadoId> {

    List<RecursoEstado> findByRecursoIdRecurso(Integer idRecurso);

    List<RecursoEstado> findByEstadoIdEstado(Integer idEstado);

    List<RecursoEstado> findByEsPrincipalProductorTrue();
}
