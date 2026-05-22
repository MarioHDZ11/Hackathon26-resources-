package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.EstadoDTO;
import com.BiSourceMX.Proyect.dto.EstadoResponse;
import com.BiSourceMX.Proyect.dto.RecursoEstadoDTO;
import com.BiSourceMX.Proyect.model.Estado;
import com.BiSourceMX.Proyect.model.RecursoEstado;
import com.BiSourceMX.Proyect.repository.EstadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
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

        List<RecursoEstadoDTO> recursos = estado.getRecursos().stream()
                .map(this::toRecursoEstadoDTO)
                .collect(Collectors.toList());

        return EstadoResponse.builder()
                .idEstado(estado.getIdEstado())
                .nombre(estado.getNombre())
                .capital(estado.getCapital())
                .datoCurioso(estado.getDatoCurioso())
                .recursos(recursos)
                .build();
    }

    @Transactional
    public EstadoDTO create(EstadoDTO dto) {
        Estado estado = Estado.builder()
                .nombre(dto.getNombre())
                .capital(dto.getCapital())
                .datoCurioso(dto.getDatoCurioso())
                .build();
        estado = estadoRepository.save(estado);
        return toDTO(estado);
    }

    @Transactional
    public EstadoDTO update(Integer id, EstadoDTO dto) {
        Estado estado = estadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con id: " + id));
        estado.setNombre(dto.getNombre());
        estado.setCapital(dto.getCapital());
        estado.setDatoCurioso(dto.getDatoCurioso());
        estado = estadoRepository.save(estado);
        return toDTO(estado);
    }

    @Transactional
    public void delete(Integer id) {
        if (!estadoRepository.existsById(id)) {
            throw new RuntimeException("Estado no encontrado con id: " + id);
        }
        estadoRepository.deleteById(id);
    }

    private EstadoDTO toDTO(Estado estado) {
        return EstadoDTO.builder()
                .idEstado(estado.getIdEstado())
                .nombre(estado.getNombre())
                .capital(estado.getCapital())
                .datoCurioso(estado.getDatoCurioso())
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
