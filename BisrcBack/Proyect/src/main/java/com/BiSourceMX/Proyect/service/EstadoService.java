package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.*;
import com.BiSourceMX.Proyect.model.*;
import com.BiSourceMX.Proyect.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final DistribucionRecursoRepository distribucionRecursoRepository;
    private final DatoCuriosoRepository datoCuriosoRepository;

    public EstadoService(EstadoRepository estadoRepository,
                         DistribucionRecursoRepository distribucionRecursoRepository,
                         DatoCuriosoRepository datoCuriosoRepository) {
        this.estadoRepository = estadoRepository;
        this.distribucionRecursoRepository = distribucionRecursoRepository;
        this.datoCuriosoRepository = datoCuriosoRepository;
    }

    public List<EstadoDTO> getAll() {
        return estadoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EstadoResponse getById(Integer id) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + id));

        List<DistribucionRecursoDTO> recursos = distribucionRecursoRepository
                .findByEstadoIdEstado(id)
                .stream()
                .map(this::toDistribucionRecursoDTO)
                .collect(Collectors.toList());

        List<DatoCuriosoDTO> datos = datoCuriosoRepository
                .findByEstadoIdEstado(id)
                .stream()
                .map(d -> DatoCuriosoDTO.builder()
                        .idDatoCurioso(d.getIdDatoCurioso())
                        .numeroDato(d.getNumeroDato())
                        .datoCurioso(d.getDatoCurioso())
                        .build())
                .collect(Collectors.toList());

        return EstadoResponse.builder()
                .idEstado(estado.getIdEstado())
                .nombreEstado(estado.getNombreEstado())
                .capital(estado.getCapital())
                .nombreRegion(estado.getRegion() != null ? estado.getRegion().getNombreRegion() : null)
                .idRegion(estado.getRegion() != null ? estado.getRegion().getIdRegion() : null)
                .nivelEstado(estado.getEstadoActual() != null ? estado.getEstadoActual().getNombreNivel() : null)
                .colorIndicador(estado.getEstadoActual() != null ? estado.getEstadoActual().getColorIndicador() : null)
                .idEstadoActual(estado.getEstadoActual() != null ? estado.getEstadoActual().getIdNivel() : null)
                .poblacionTotal(estado.getPoblacionTotal())
                .superficieKm2(estado.getSuperficieKm2())
                .recursos(recursos)
                .datosCuriosos(datos)
                .build();
    }

    public EstadoDTO toDTO(Estado estado) {
        return EstadoDTO.builder()
                .idEstado(estado.getIdEstado())
                .nombreEstado(estado.getNombreEstado())
                .capital(estado.getCapital())
                .nombreRegion(estado.getRegion() != null ? estado.getRegion().getNombreRegion() : null)
                .idRegion(estado.getRegion() != null ? estado.getRegion().getIdRegion() : null)
                .nivelEstado(estado.getEstadoActual() != null ? estado.getEstadoActual().getNombreNivel() : null)
                .colorIndicador(estado.getEstadoActual() != null ? estado.getEstadoActual().getColorIndicador() : null)
                .idEstadoActual(estado.getEstadoActual() != null ? estado.getEstadoActual().getIdNivel() : null)
                .poblacionTotal(estado.getPoblacionTotal())
                .superficieKm2(estado.getSuperficieKm2())
                .build();
    }

    private DistribucionRecursoDTO toDistribucionRecursoDTO(DistribucionRecurso dr) {
        return DistribucionRecursoDTO.builder()
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
                .build();
    }
}
