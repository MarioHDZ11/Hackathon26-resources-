package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recursos_estado")
@IdClass(RecursoEstadoId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoEstado {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado", nullable = false)
    private Estado estado;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_recurso", referencedColumnName = "id_recurso", nullable = false)
    private Recurso recurso;

    @Column(name = "es_principal_productor")
    private Boolean esPrincipalProductor;

    @Column(name = "nota_educativa", length = 255)
    private String notaEducativa;
}
