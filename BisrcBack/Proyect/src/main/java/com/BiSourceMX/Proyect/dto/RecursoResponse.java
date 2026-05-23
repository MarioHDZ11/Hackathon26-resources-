package com.BiSourceMX.Proyect.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoResponse {
    private Integer idRecurso;
    private String nombreRecurso;
    private String nombreCategoria;
    private Integer idCategoria;
    private String descripcion;
    private String unidadMedida;
    private String icono;
    private List<DistribucionRecursoDTO> estados;
}
