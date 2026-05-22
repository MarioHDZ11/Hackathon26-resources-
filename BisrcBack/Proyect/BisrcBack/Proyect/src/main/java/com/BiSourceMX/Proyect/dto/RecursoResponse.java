package com.BiSourceMX.Proyect.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoResponse {
    private Integer idRecurso;
    private String nombre;
    private String categoria;
    private String iconoUrl;
    private String descripcion;
    private List<RecursoEstadoDTO> estados;
}
