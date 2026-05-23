package com.bisourcesmx.bisources.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estados")
public class Estado {

    @Id
    @Column(name = "id_estado")
    private Integer id;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "capital", nullable = false, length = 100)
    private String capital;

    @Column(name = "codigo_3", length = 3)
    private String codigo3;

    @Column(name = "simbolo_emoji", length = 10)
    private String simboloEmoji;

    @Column(name = "descripcion_corta", length = 200)
    private String descripcionCorta;

    @Column(name = "svg_path", columnDefinition = "TEXT")
    private String svgPath;

    @Column(name = "es_costero")
    private Boolean esCostero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @Column(name = "estado_actual", nullable = false)
    private Integer estadoActual;

    @Column(name = "poblacion_total")
    private Long poblacionTotal;

    @Column(name = "superficie_km2")
    private Double superficieKm2;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }
    public String getCodigo3() { return codigo3; }
    public void setCodigo3(String codigo3) { this.codigo3 = codigo3; }
    public String getSimboloEmoji() { return simboloEmoji; }
    public void setSimboloEmoji(String simboloEmoji) { this.simboloEmoji = simboloEmoji; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public void setDescripcionCorta(String descripcionCorta) { this.descripcionCorta = descripcionCorta; }
    public String getSvgPath() { return svgPath; }
    public void setSvgPath(String svgPath) { this.svgPath = svgPath; }
    public Boolean getEsCostero() { return esCostero; }
    public void setEsCostero(Boolean esCostero) { this.esCostero = esCostero; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public Integer getEstadoActual() { return estadoActual; }
    public void setEstadoActual(Integer estadoActual) { this.estadoActual = estadoActual; }
    public Long getPoblacionTotal() { return poblacionTotal; }
    public void setPoblacionTotal(Long poblacionTotal) { this.poblacionTotal = poblacionTotal; }
    public Double getSuperficieKm2() { return superficieKm2; }
    public void setSuperficieKm2(Double superficieKm2) { this.superficieKm2 = superficieKm2; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
