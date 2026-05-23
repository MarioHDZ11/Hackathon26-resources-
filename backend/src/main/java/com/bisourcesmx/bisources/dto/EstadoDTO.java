package com.bisourcesmx.bisources.dto;

public class EstadoDTO {
    private String id;
    private String name;
    private String capital;
    private String poblacion;
    private boolean isCoastal;
    private String simbolo;
    private String descripcion;
    private String d;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCapital() { return capital; }
    public void setCapital(String capital) { this.capital = capital; }
    public String getPoblacion() { return poblacion; }
    public void setPoblacion(String poblacion) { this.poblacion = poblacion; }
    public boolean getIsCoastal() { return isCoastal; }
    public void setIsCoastal(boolean coastal) { isCoastal = coastal; }
    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getD() { return d; }
    public void setD(String d) { this.d = d; }
}
