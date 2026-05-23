package com.BiSourceMX.Proyect.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoResponse {
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
    private List<DistribucionRecursoDTO> recursos;
    private List<DatoCuriosoDTO> datosCuriosos;
}
