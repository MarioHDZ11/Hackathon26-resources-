package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recurso {

    @Id
    @Column(name = "id_recurso")
    private Integer idRecurso;

    @Column(name = "nombre_recurso", nullable = false, unique = true, length = 100)
    private String nombreRecurso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaRecurso categoria;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;

    @Column(name = "icono", length = 50)
    private String icono;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
