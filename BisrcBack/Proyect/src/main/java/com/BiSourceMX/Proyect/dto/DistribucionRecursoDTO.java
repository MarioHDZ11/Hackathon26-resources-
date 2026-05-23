package com.BiSourceMX.Proyect.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistribucionRecursoDTO {
    private Integer idDistribucion;
    private Integer idEstado;
    private String nombreEstado;
    private Integer idRecurso;
    private String nombreRecurso;
    private String iconoRecurso;
    private Double cantidadDisponible;
    private Double cantidadAsignada;
    private Double cantidadReserva;
    private Double indiceDisponibilidad;
    private LocalDate fechaActualizacion;
}
