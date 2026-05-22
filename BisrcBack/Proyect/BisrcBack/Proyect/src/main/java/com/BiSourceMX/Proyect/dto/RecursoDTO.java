package com.BiSourceMX.Proyect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoDTO {
    private Integer idRecurso;
    private String nombre;
    private String categoria;
    private String iconoUrl;
    private String descripcion;
}
