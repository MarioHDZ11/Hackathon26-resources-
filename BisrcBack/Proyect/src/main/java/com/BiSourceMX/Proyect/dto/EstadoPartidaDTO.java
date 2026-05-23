package com.BiSourceMX.Proyect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoPartidaDTO {
    private Integer idEstadoPartida;
    private Integer idPartida;
    private Integer idEstadoRef;
    private String nombreEstado;
    private Long poblacion;
    private Integer bienestar;
    private Integer presupuesto;
    private Double agua;
    private Double energia;
    private Double alimento;
    private Double salud;
    private Double sostenibilidad;
    private Double infraestructura;
    private Double desarrolloSociocultural;
    private Double distribucion;
}
