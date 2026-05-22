package com.BiSourceMX.Proyect.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoResponse {
    private Integer idEstado;
    private String nombre;
    private String capital;
    private String datoCurioso;
    private List<RecursoEstadoDTO> recursos;
}
