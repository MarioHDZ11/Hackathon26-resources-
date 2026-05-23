package com.BiSourceMX.Proyect.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaDTO {
    private Integer idPartida;
    private String nombre;
    private LocalDateTime fechaCreacion;
    private Boolean activa;
    private Integer tickCount;
    private String datosJson;
    private List<EstadoPartidaDTO> estadosPartida;
}
