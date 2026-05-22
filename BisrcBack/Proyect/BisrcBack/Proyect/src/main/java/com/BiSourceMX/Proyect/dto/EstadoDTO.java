package com.BiSourceMX.Proyect.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoDTO {
    private Integer idEstado;
    private String nombre;
    private String capital;
    private String datoCurioso;
}
