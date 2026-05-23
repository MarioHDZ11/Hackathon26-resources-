package com.bisourcesmx.bisources.repository;

import com.bisourcesmx.bisources.model.MunicipioImportante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MunicipioImportanteRepository extends JpaRepository<MunicipioImportante, Integer> {
    List<MunicipioImportante> findByEstadoId(Integer estadoId);
}
