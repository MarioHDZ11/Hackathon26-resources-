package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.RecursoEstadoDTO;
import com.BiSourceMX.Proyect.model.Estado;
import com.BiSourceMX.Proyect.model.Recurso;
import com.BiSourceMX.Proyect.model.RecursoEstado;
import com.BiSourceMX.Proyect.model.RecursoEstadoId;
import com.BiSourceMX.Proyect.repository.EstadoRepository;
import com.BiSourceMX.Proyect.repository.RecursoEstadoRepository;
import com.BiSourceMX.Proyect.repository.RecursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecursoEstadoService {

    private final RecursoEstadoRepository recursoEstadoRepository;
    private final EstadoRepository estadoRepository;
    private final RecursoRepository recursoRepository;

    public RecursoEstadoService(RecursoEstadoRepository recursoEstadoRepository,
                                EstadoRepository estadoRepository,
                                RecursoRepository recursoRepository) {
        this.recursoEstadoRepository = recursoEstadoRepository;
        this.estadoRepository = estadoRepository;
        this.recursoRepository = recursoRepository;
    }

    public List<RecursoEstadoDTO> getByEstado(Integer idEstado) {
        return recursoEstadoRepository.findByEstadoIdEstado(idEstado)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<RecursoEstadoDTO> getByRecurso(Integer idRecurso) {
        return recursoEstadoRepository.findByRecursoIdRecurso(idRecurso)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecursoEstadoDTO associate(Integer idEstado, Integer idRecurso, RecursoEstadoDTO dto) {
        Estado estado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + idEstado));
        Recurso recurso = recursoRepository.findById(idRecurso)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con id: " + idRecurso));

        RecursoEstadoId id = new RecursoEstadoId(estado, recurso);
        if (recursoEstadoRepository.existsById(id)) {
            throw new RuntimeException("La relación ya existe entre el estado " + idEstado + " y el recurso " + idRecurso);
        }

        RecursoEstado re = RecursoEstado.builder()
                .estado(estado)
                .recurso(recurso)
                .esPrincipalProductor(dto.getEsPrincipalProductor())
                .notaEducativa(dto.getNotaEducativa())
                .build();

        re = recursoEstadoRepository.save(re);
        return toDTO(re);
    }

    @Transactional
    public RecursoEstadoDTO update(Integer idEstado, Integer idRecurso, RecursoEstadoDTO dto) {
        Estado estado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + idEstado));
        Recurso recurso = recursoRepository.findById(idRecurso)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con id: " + idRecurso));

        RecursoEstadoId id = new RecursoEstadoId(estado, recurso);
        RecursoEstado re = recursoEstadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada"));

        re.setEsPrincipalProductor(dto.getEsPrincipalProductor());
        re.setNotaEducativa(dto.getNotaEducativa());
        re = recursoEstadoRepository.save(re);
        return toDTO(re);
    }

    @Transactional
    public void disassociate(Integer idEstado, Integer idRecurso) {
        Estado estado = estadoRepository.findById(idEstado)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + idEstado));
        Recurso recurso = recursoRepository.findById(idRecurso)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con id: " + idRecurso));

        RecursoEstadoId id = new RecursoEstadoId(estado, recurso);
        if (!recursoEstadoRepository.existsById(id)) {
            throw new RuntimeException("Relación no encontrada");
        }
        recursoEstadoRepository.deleteById(id);
    }

    private RecursoEstadoDTO toDTO(RecursoEstado re) {
        return RecursoEstadoDTO.builder()
                .idEstado(re.getEstado().getIdEstado())
                .nombreEstado(re.getEstado().getNombre())
                .idRecurso(re.getRecurso().getIdRecurso())
                .nombreRecurso(re.getRecurso().getNombre())
                .esPrincipalProductor(re.getEsPrincipalProductor())
                .notaEducativa(re.getNotaEducativa())
                .build();
    }
}
