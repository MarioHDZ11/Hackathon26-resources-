package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias_recurso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaRecurso {

    @Id
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre_categoria", nullable = false, unique = true, length = 100)
    private String nombreCategoria;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "icono", length = 50)
    private String icono;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
