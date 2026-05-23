package com.BiSourceMX.Proyect.service;

import com.BiSourceMX.Proyect.dto.EstadoInfoDTO;
import java.util.List;

public interface EstadoDataService {

    List<EstadoInfoDTO> obtenerTodos();

    EstadoInfoDTO obtenerPorNombre(String nombre);

    List<EstadoInfoDTO> filtrarPorRegion(String region);

    List<EstadoInfoDTO> filtrarPorIntercambioAlto(String recurso);
}
