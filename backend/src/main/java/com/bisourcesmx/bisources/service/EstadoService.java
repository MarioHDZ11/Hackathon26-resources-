package com.bisourcesmx.bisources.service;

import com.bisourcesmx.bisources.dto.EstadoDTO;
import com.bisourcesmx.bisources.dto.MunicipioDTO;
import com.bisourcesmx.bisources.model.Estado;
import com.bisourcesmx.bisources.model.MunicipioImportante;
import com.bisourcesmx.bisources.repository.DatoCuriosoRepository;
import com.bisourcesmx.bisources.repository.EstadoRepository;
import com.bisourcesmx.bisources.repository.MunicipioImportanteRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final DatoCuriosoRepository datoCuriosoRepository;
    private final MunicipioImportanteRepository municipioImportanteRepository;

    private static final List<String> RECURSOS = List.of("Agua", "Energia", "Alimento", "Salud", "Infraestructura", "Presupuesto");

    public EstadoService(EstadoRepository estadoRepository, DatoCuriosoRepository datoCuriosoRepository, MunicipioImportanteRepository municipioImportanteRepository) {
        this.estadoRepository = estadoRepository;
        this.datoCuriosoRepository = datoCuriosoRepository;
        this.municipioImportanteRepository = municipioImportanteRepository;
    }

    public List<EstadoDTO> getAllEstados() {
        return estadoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EstadoDTO getEstadoById(Integer id) {
        return estadoRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public EstadoDTO getEstadoByNombre(String nombre) {
        return estadoRepository.findByNombre(nombre).map(this::toDTO).orElse(null);
    }

    public EstadoDTO getEstadoByCodigo3(String codigo3) {
        return estadoRepository.findByCodigo3(codigo3).map(this::toDTO).orElse(null);
    }

    public List<MunicipioDTO> getMunicipiosByEstadoNombre(String nombre) {
        Optional<Estado> opt = estadoRepository.findByNombre(nombre);
        if (opt.isEmpty()) return generateFallbackMunicipios(nombre);
        Estado estado = opt.get();
        List<MunicipioDTO> municipios = new ArrayList<>();
        int seed = estado.getId() * 7 + 13;
        municipios.add(new MunicipioDTO(estado.getCapital(), 45 + (seed * 3) % 51, RECURSOS.get(seed % RECURSOS.size())));
        List<MunicipioImportante> importantes = municipioImportanteRepository.findByEstadoId(estado.getId());
        if (importantes.isEmpty()) {
            String[] prefijos = {"Norte", "Sur", "Este", "Oeste"};
            for (int i = 0; i < prefijos.length; i++) {
                int hash = (estado.getNombre().length() * 7 + i * 13) % 41;
                municipios.add(new MunicipioDTO(
                        estado.getCapital() + " " + prefijos[i],
                        45 + hash,
                        RECURSOS.get((estado.getNombre().length() + i) % RECURSOS.size())
                ));
            }
        } else {
            for (int i = 0; i < importantes.size(); i++) {
                int hash = (estado.getNombre().length() * 7 + i * 13) % 41;
                municipios.add(new MunicipioDTO(
                        importantes.get(i).getNombre(),
                        45 + hash,
                        RECURSOS.get((estado.getNombre().length() + i) % RECURSOS.size())
                ));
            }
        }
        return municipios;
    }

    public String getRandomDatoCurioso(Integer estadoId) {
        return datoCuriosoRepository.findRandomDatoByEstadoId(estadoId);
    }

    private List<MunicipioDTO> generateFallbackMunicipios(String nombre) {
        List<MunicipioDTO> list = new ArrayList<>();
        String[] prefijos = {"Norte", "Sur", "Centro", "Este", "Oeste"};
        for (int i = 0; i < prefijos.length; i++) {
            list.add(new MunicipioDTO(
                    nombre + " " + prefijos[i],
                    45 + ((nombre.length() * 7 + i * 13) % 41),
                    RECURSOS.get((nombre.length() + i) % RECURSOS.size())
            ));
        }
        return list;
    }

    private EstadoDTO toDTO(Estado e) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(e.getCodigo3() != null ? e.getCodigo3() : String.valueOf(e.getId()));
        dto.setName(e.getNombre());
        dto.setCapital(e.getCapital());
        dto.setPoblacion(e.getPoblacionTotal() != null ? String.valueOf(e.getPoblacionTotal()) : "0");
        dto.setIsCoastal(e.getEsCostero() != null && e.getEsCostero());
        dto.setSimbolo(e.getSimboloEmoji());
        dto.setDescripcion(e.getDescripcionCorta());
        dto.setD(e.getSvgPath());
        return dto;
    }
}
