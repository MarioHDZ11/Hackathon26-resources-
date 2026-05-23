package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estados_partida")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoPartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_partida")
    private Integer idEstadoPartida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partida", nullable = false)
    private Partida partida;

    @Column(name = "id_estado_ref", nullable = false)
    private Integer idEstadoRef;

    @Column(name = "nombre_estado", length = 50)
    private String nombreEstado;

    @Column(name = "poblacion")
    @Builder.Default
    private Long poblacion = 0L;

    @Column(name = "bienestar")
    @Builder.Default
    private Integer bienestar = 50;

    @Column(name = "presupuesto")
    @Builder.Default
    private Integer presupuesto = 50;

    @Column(name = "agua")
    @Builder.Default
    private Double agua = 50.0;

    @Column(name = "energia")
    @Builder.Default
    private Double energia = 50.0;

    @Column(name = "alimento")
    @Builder.Default
    private Double alimento = 50.0;

    @Column(name = "salud")
    @Builder.Default
    private Double salud = 50.0;

    @Column(name = "sostenibilidad")
    @Builder.Default
    private Double sostenibilidad = 50.0;

    @Column(name = "infraestructura")
    @Builder.Default
    private Double infraestructura = 50.0;

    @Column(name = "desarrollo_sociocultural")
    @Builder.Default
    private Double desarrolloSociocultural = 50.0;

    @Column(name = "distribucion")
    @Builder.Default
    private Double distribucion = 50.0;
}
