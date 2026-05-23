package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "partidas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partida")
    private Integer idPartida;

    @Column(name = "nombre", length = 100)
    @Builder.Default
    private String nombre = "Partida sin nombre";

    @Column(name = "fecha_creacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "activa")
    @Builder.Default
    private Boolean activa = true;

    @Column(name = "tick_count")
    @Builder.Default
    private Integer tickCount = 0;

    @Column(name = "datos_json", columnDefinition = "LONGTEXT")
    private String datosJson;

    @OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<EstadoPartida> estadosPartida = new HashSet<>();
}
