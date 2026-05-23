package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "niveles_estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelEstado {

    @Id
    @Column(name = "id_nivel")
    private Integer idNivel;

    @Column(name = "nombre_nivel", nullable = false, length = 50)
    private String nombreNivel;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "color_indicador", length = 7)
    private String colorIndicador;

    @Column(name = "prioridad")
    private Integer prioridad;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
