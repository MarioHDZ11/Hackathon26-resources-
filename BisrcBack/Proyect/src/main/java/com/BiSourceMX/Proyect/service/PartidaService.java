package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.EstadoPartidaDTO;
import com.BiSourceMX.Proyect.dto.PartidaDTO;
import com.BiSourceMX.Proyect.model.EstadoPartida;
import com.BiSourceMX.Proyect.model.Partida;
import com.BiSourceMX.Proyect.repository.EstadoPartidaRepository;
import com.BiSourceMX.Proyect.repository.PartidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final EstadoPartidaRepository estadoPartidaRepository;

    public PartidaService(PartidaRepository partidaRepository, EstadoPartidaRepository estadoPartidaRepository) {
        this.partidaRepository = partidaRepository;
        this.estadoPartidaRepository = estadoPartidaRepository;
    }

    @Transactional(readOnly = true)
    public PartidaDTO getUltimaActiva() {
        Partida partida = partidaRepository.findFirstByActivaTrueOrderByFechaCreacionDesc()
                .orElse(null);
        if (partida == null) return null;
        return toDTO(partida);
    }

    @Transactional
    public PartidaDTO crearNueva(PartidaDTO dto) {
        Partida partida = Partida.builder()
                .nombre(dto.getNombre() != null ? dto.getNombre() : "Partida " + java.time.LocalDateTime.now())
                .activa(true)
                .tickCount(0)
                .datosJson(dto.getDatosJson())
                .build();
        partida = partidaRepository.save(partida);

        if (dto.getEstadosPartida() != null) {
            for (EstadoPartidaDTO edto : dto.getEstadosPartida()) {
                saveEstadoPartida(partida, edto);
            }
        }

        return toDTO(partida);
    }

    @Transactional
    public PartidaDTO actualizarPartida(Integer idPartida, PartidaDTO dto) {
        Partida partida = partidaRepository.findById(idPartida)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada con id: " + idPartida));

        partida.setTickCount(dto.getTickCount() != null ? dto.getTickCount() : partida.getTickCount());
        partida.setNombre(dto.getNombre() != null ? dto.getNombre() : partida.getNombre());
        partida.setActiva(dto.getActiva() != null ? dto.getActiva() : partida.getActiva());
        partida.setDatosJson(dto.getDatosJson() != null ? dto.getDatosJson() : partida.getDatosJson());

        if (dto.getEstadosPartida() != null) {
            estadoPartidaRepository.deleteByPartidaIdPartida(idPartida);
            for (EstadoPartidaDTO edto : dto.getEstadosPartida()) {
                saveEstadoPartida(partida, edto);
            }
        }

        return toDTO(partida);
    }

    private void saveEstadoPartida(Partida partida, EstadoPartidaDTO edto) {
        EstadoPartida ep = EstadoPartida.builder()
                .partida(partida)
                .idEstadoRef(edto.getIdEstadoRef())
                .nombreEstado(edto.getNombreEstado())
                .poblacion(edto.getPoblacion())
                .bienestar(edto.getBienestar())
                .presupuesto(edto.getPresupuesto())
                .agua(edto.getAgua())
                .energia(edto.getEnergia())
                .alimento(edto.getAlimento())
                .salud(edto.getSalud())
                .sostenibilidad(edto.getSostenibilidad())
                .infraestructura(edto.getInfraestructura())
                .desarrolloSociocultural(edto.getDesarrolloSociocultural())
                .distribucion(edto.getDistribucion())
                .build();
        estadoPartidaRepository.save(ep);
    }

    @Transactional(readOnly = true)
    public PartidaDTO getById(Integer id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partida no encontrada con id: " + id));
        return toDTO(partida);
    }

    private PartidaDTO toDTO(Partida partida) {
        List<EstadoPartida> estados = estadoPartidaRepository.findByPartidaIdPartida(partida.getIdPartida());
        List<EstadoPartidaDTO> estadosDTO = estados.stream()
                .map(this::toEstadoDTO)
                .collect(Collectors.toList());

        return PartidaDTO.builder()
                .idPartida(partida.getIdPartida())
                .nombre(partida.getNombre())
                .fechaCreacion(partida.getFechaCreacion())
                .activa(partida.getActiva())
                .tickCount(partida.getTickCount())
                .datosJson(partida.getDatosJson())
                .estadosPartida(estadosDTO)
                .build();
    }

    private EstadoPartidaDTO toEstadoDTO(EstadoPartida ep) {
        return EstadoPartidaDTO.builder()
                .idEstadoPartida(ep.getIdEstadoPartida())
                .idPartida(ep.getPartida().getIdPartida())
                .idEstadoRef(ep.getIdEstadoRef())
                .nombreEstado(ep.getNombreEstado())
                .poblacion(ep.getPoblacion())
                .bienestar(ep.getBienestar())
                .presupuesto(ep.getPresupuesto())
                .agua(ep.getAgua())
                .energia(ep.getEnergia())
                .alimento(ep.getAlimento())
                .salud(ep.getSalud())
                .sostenibilidad(ep.getSostenibilidad())
                .infraestructura(ep.getInfraestructura())
                .desarrolloSociocultural(ep.getDesarrolloSociocultural())
                .distribucion(ep.getDistribucion())
                .build();
    }
}
