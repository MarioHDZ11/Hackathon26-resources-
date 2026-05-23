package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.DistribucionRecursoDTO;
import com.BiSourceMX.Proyect.dto.RecursoDTO;
import com.BiSourceMX.Proyect.dto.RecursoResponse;
import com.BiSourceMX.Proyect.model.Recurso;
import com.BiSourceMX.Proyect.repository.DistribucionRecursoRepository;
import com.BiSourceMX.Proyect.repository.RecursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final DistribucionRecursoRepository distribucionRecursoRepository;

    public RecursoService(RecursoRepository recursoRepository,
                          DistribucionRecursoRepository distribucionRecursoRepository) {
        this.recursoRepository = recursoRepository;
        this.distribucionRecursoRepository = distribucionRecursoRepository;
    }

    public List<RecursoDTO> getAll() {
        return recursoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecursoResponse getById(Integer id) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con id: " + id));

        List<DistribucionRecursoDTO> estados = distribucionRecursoRepository
                .findByRecursoIdRecurso(id)
                .stream()
                .map(dr -> DistribucionRecursoDTO.builder()
                        .idDistribucion(dr.getIdDistribucion())
                        .idEstado(dr.getEstado().getIdEstado())
                        .nombreEstado(dr.getEstado().getNombreEstado())
                        .idRecurso(dr.getRecurso().getIdRecurso())
                        .nombreRecurso(dr.getRecurso().getNombreRecurso())
                        .iconoRecurso(dr.getRecurso().getIcono())
                        .cantidadDisponible(dr.getCantidadDisponible())
                        .cantidadAsignada(dr.getCantidadAsignada())
                        .cantidadReserva(dr.getCantidadReserva())
                        .indiceDisponibilidad(dr.getIndiceDisponibilidad())
                        .fechaActualizacion(dr.getFechaActualizacion())
                        .build())
                .collect(Collectors.toList());

        return RecursoResponse.builder()
                .idRecurso(recurso.getIdRecurso())
                .nombreRecurso(recurso.getNombreRecurso())
                .nombreCategoria(recurso.getCategoria() != null ? recurso.getCategoria().getNombreCategoria() : null)
                .idCategoria(recurso.getCategoria() != null ? recurso.getCategoria().getIdCategoria() : null)
                .descripcion(recurso.getDescripcion())
                .unidadMedida(recurso.getUnidadMedida())
                .icono(recurso.getIcono())
                .estados(estados)
                .build();
    }

    private RecursoDTO toDTO(Recurso recurso) {
        return RecursoDTO.builder()
                .idRecurso(recurso.getIdRecurso())
                .nombreRecurso(recurso.getNombreRecurso())
                .nombreCategoria(recurso.getCategoria() != null ? recurso.getCategoria().getNombreCategoria() : null)
                .idCategoria(recurso.getCategoria() != null ? recurso.getCategoria().getIdCategoria() : null)
                .descripcion(recurso.getDescripcion())
                .unidadMedida(recurso.getUnidadMedida())
                .icono(recurso.getIcono())
                .build();
    }
}
