package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.RecursoDTO;
import com.BiSourceMX.Proyect.dto.RecursoEstadoDTO;
import com.BiSourceMX.Proyect.dto.RecursoResponse;
import com.BiSourceMX.Proyect.model.Recurso;
import com.BiSourceMX.Proyect.model.RecursoEstado;
import com.BiSourceMX.Proyect.repository.RecursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;

    public RecursoService(RecursoRepository recursoRepository) {
        this.recursoRepository = recursoRepository;
    }

    public List<RecursoDTO> getAll() {
        return recursoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RecursoResponse getById(Integer id) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con id: " + id));

        List<RecursoEstadoDTO> estados = recurso.getEstados().stream()
                .map(this::toRecursoEstadoDTO)
                .collect(Collectors.toList());

        return RecursoResponse.builder()
                .idRecurso(recurso.getIdRecurso())
                .nombre(recurso.getNombre())
                .categoria(recurso.getCategoria())
                .iconoUrl(recurso.getIconoUrl())
                .descripcion(recurso.getDescripcion())
                .estados(estados)
                .build();
    }

    public List<RecursoDTO> getByCategoria(String categoria) {
        return recursoRepository.findByCategoria(categoria)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RecursoDTO create(RecursoDTO dto) {
        Recurso recurso = Recurso.builder()
                .nombre(dto.getNombre())
                .categoria(dto.getCategoria())
                .iconoUrl(dto.getIconoUrl())
                .descripcion(dto.getDescripcion())
                .build();
        recurso = recursoRepository.save(recurso);
        return toDTO(recurso);
    }

    @Transactional
    public RecursoDTO update(Integer id, RecursoDTO dto) {
        Recurso recurso = recursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado con id: " + id));
        recurso.setNombre(dto.getNombre());
        recurso.setCategoria(dto.getCategoria());
        recurso.setIconoUrl(dto.getIconoUrl());
        recurso.setDescripcion(dto.getDescripcion());
        recurso = recursoRepository.save(recurso);
        return toDTO(recurso);
    }

    @Transactional
    public void delete(Integer id) {
        if (!recursoRepository.existsById(id)) {
            throw new RuntimeException("Recurso no encontrado con id: " + id);
        }
        recursoRepository.deleteById(id);
    }

    private RecursoDTO toDTO(Recurso recurso) {
        return RecursoDTO.builder()
                .idRecurso(recurso.getIdRecurso())
                .nombre(recurso.getNombre())
                .categoria(recurso.getCategoria())
                .iconoUrl(recurso.getIconoUrl())
                .descripcion(recurso.getDescripcion())
                .build();
    }

    private RecursoEstadoDTO toRecursoEstadoDTO(RecursoEstado re) {
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
