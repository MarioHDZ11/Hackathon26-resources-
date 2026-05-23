package com.BiSourceMX.Proyect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoDTO {
    private Integer idEstado;
    private String nombreEstado;
    private String capital;
    private String nombreRegion;
    private Integer idRegion;
    private String nivelEstado;
    private String colorIndicador;
    private Integer idEstadoActual;
    private Long poblacionTotal;
    private Double superficieKm2;
}
