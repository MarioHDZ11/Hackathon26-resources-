package com.BiSourceMX.Proyect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoEstadoDTO {
    private Integer idEstado;
    private String nombreEstado;
    private Integer idRecurso;
    private String nombreRecurso;
    private Boolean esPrincipalProductor;
    private String notaEducativa;
}
