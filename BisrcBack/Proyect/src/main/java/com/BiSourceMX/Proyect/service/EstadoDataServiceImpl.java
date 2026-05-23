package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.DetalleIntercambioDTO;
import com.BiSourceMX.Proyect.dto.EstadoInfoDTO;
import com.BiSourceMX.Proyect.dto.IntercambioDTO;
import com.BiSourceMX.Proyect.exception.EstadoNotFoundException;
import com.BiSourceMX.Proyect.staticdata.EstadoData;
import com.BiSourceMX.Proyect.staticdata.EstadoData.DetalleIntercambio;
import com.BiSourceMX.Proyect.staticdata.EstadoData.Estado;
import com.BiSourceMX.Proyect.staticdata.EstadoData.Intercambio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class EstadoDataServiceImpl implements EstadoDataService {

    private final List<Estado> estados = EstadoData.ESTADOS;

    private static final Map<String, Function<Intercambio, DetalleIntercambio>> RECURSO_EXTRACTORS = Map.of(
        "agua", Intercambio::getAgua,
        "energia", Intercambio::getEnergia,
        "presupuesto", Intercambio::getPresupuesto,
        "alimento", Intercambio::getAlimento,
        "trabajadores", Intercambio::getTrabajadores
    );

    @Override
    public List<EstadoInfoDTO> obtenerTodos() {
        return estados.stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public EstadoInfoDTO obtenerPorNombre(String nombre) {
        return estados.stream()
                .filter(e -> e.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .map(this::toDTO)
                .orElseThrow(() -> new EstadoNotFoundException(nombre));
    }

    @Override
    public List<EstadoInfoDTO> filtrarPorRegion(String region) {
        return estados.stream()
                .filter(e -> e.getRegion().equalsIgnoreCase(region))
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<EstadoInfoDTO> filtrarPorIntercambioAlto(String recurso) {
        Function<Intercambio, DetalleIntercambio> extractor = RECURSO_EXTRACTORS.get(recurso.toLowerCase());
        if (extractor == null) {
            throw new IllegalArgumentException("Recurso inv\u00e1lido: " + recurso
                    + ". Los recursos v\u00e1lidos son: " + String.join(", ", RECURSO_EXTRACTORS.keySet()));
        }
        return estados.stream()
                .filter(e -> {
                    DetalleIntercambio detalle = extractor.apply(e.getIntercambio());
                    return detalle != null && "alto".equalsIgnoreCase(detalle.getNivel());
                })
                .map(this::toDTO)
                .toList();
    }

    private EstadoInfoDTO toDTO(Estado estado) {
        return new EstadoInfoDTO(
                estado.getNombre(),
                estado.getRegion(),
                estado.getClima(),
                estado.getRiesgos(),
                estado.getAguaInfo(),
                estado.getLuzInfo(),
                toIntercambioDTO(estado.getIntercambio())
        );
    }

    private IntercambioDTO toIntercambioDTO(Intercambio intercambio) {
        return new IntercambioDTO(
                toDetalleDTO(intercambio.getAgua()),
                toDetalleDTO(intercambio.getEnergia()),
                toDetalleDTO(intercambio.getPresupuesto()),
                toDetalleDTO(intercambio.getAlimento()),
                toDetalleDTO(intercambio.getTrabajadores())
        );
    }

    private DetalleIntercambioDTO toDetalleDTO(DetalleIntercambio detalle) {
        if (detalle == null) return null;
        return new DetalleIntercambioDTO(detalle.getNivel(), detalle.getRazon(), detalle.getComo());
    }
}
