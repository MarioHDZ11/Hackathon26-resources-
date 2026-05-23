package com.BiSourceMX.Proyect.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "datos_curiosos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatoCurioso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dato_curioso")
    private Integer idDatoCurioso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "numero_dato", nullable = false)
    private Integer numeroDato;

    @Column(name = "dato_curioso", columnDefinition = "TEXT", nullable = false)
    private String datoCurioso;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
