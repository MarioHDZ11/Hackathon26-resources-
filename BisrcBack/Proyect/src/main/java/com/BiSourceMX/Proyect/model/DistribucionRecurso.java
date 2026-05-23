package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "distribucion_recursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistribucionRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distribucion")
    private Integer idDistribucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recurso", nullable = false)
    private Recurso recurso;

    @Column(name = "cantidad_disponible", nullable = false)
    private Double cantidadDisponible;

    @Column(name = "cantidad_asignada")
    private Double cantidadAsignada;

    @Column(name = "cantidad_reserva")
    private Double cantidadReserva;

    @Column(name = "indice_disponibilidad")
    private Double indiceDisponibilidad;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDate fechaActualizacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
