package com.BiSourceMX.Proyect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoDTO {
    private Integer idRecurso;
    private String nombreRecurso;
    private String nombreCategoria;
    private Integer idCategoria;
    private String descripcion;
    private String unidadMedida;
    private String icono;
}
