package com.bisourcesmx.bisources.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "datos_curiosos")
public class DatoCurioso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dato_curioso")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "numero_dato", nullable = false)
    private Integer numeroDato;

    @Column(name = "dato_curioso", nullable = false, columnDefinition = "TEXT")
    private String datoCurioso;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Integer getNumeroDato() { return numeroDato; }
    public void setNumeroDato(Integer numeroDato) { this.numeroDato = numeroDato; }
    public String getDatoCurioso() { return datoCurioso; }
    public void setDatoCurioso(String datoCurioso) { this.datoCurioso = datoCurioso; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
