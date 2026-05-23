package com.BiSourceMX.Proyect.dto;

import java.util.List;

public record EstadoInfoDTO(
    String nombre,
    String region,
    String clima,
    List<String> riesgos,
    String aguaInfo,
    String luzInfo,
    IntercambioDTO intercambio
) {}
