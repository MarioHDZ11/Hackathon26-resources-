package com.bisourcesmx.bisources.dto;

public class MunicipioDTO {
    private String nombre;
    private int bienestar;
    private String recurso;

    public MunicipioDTO() {}

    public MunicipioDTO(String nombre, int bienestar, String recurso) {
        this.nombre = nombre;
        this.bienestar = bienestar;
        this.recurso = recurso;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getBienestar() { return bienestar; }
    public void setBienestar(int bienestar) { this.bienestar = bienestar; }
    public String getRecurso() { return recurso; }
    public void setRecurso(String recurso) { this.recurso = recurso; }
}
