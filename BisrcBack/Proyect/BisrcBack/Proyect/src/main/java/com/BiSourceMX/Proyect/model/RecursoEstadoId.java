package com.BiSourceMX.Proyect.model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoEstadoId implements Serializable {

    private Estado estado;
    private Recurso recurso;
}
