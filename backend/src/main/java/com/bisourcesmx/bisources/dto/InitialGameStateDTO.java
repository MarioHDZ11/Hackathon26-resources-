package com.bisourcesmx.bisources.dto;

import java.util.Map;

public class InitialGameStateDTO {
    private Map<String, EstadoGameDTO> estados;
    private FederalDTO federal;

    public Map<String, EstadoGameDTO> getEstados() { return estados; }
    public void setEstados(Map<String, EstadoGameDTO> estados) { this.estados = estados; }
    public FederalDTO getFederal() { return federal; }
    public void setFederal(FederalDTO federal) { this.federal = federal; }

    public static class EstadoGameDTO {
        private String nombre;
        private long poblacion;
        private int bienestar;
        private int presupuesto;
        private Map<String, Double> recursos;
        private InfraestructuraDTO infraestructura;

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public long getPoblacion() { return poblacion; }
        public void setPoblacion(long poblacion) { this.poblacion = poblacion; }
        public int getBienestar() { return bienestar; }
        public void setBienestar(int bienestar) { this.bienestar = bienestar; }
        public int getPresupuesto() { return presupuesto; }
        public void setPresupuesto(int presupuesto) { this.presupuesto = presupuesto; }
        public Map<String, Double> getRecursos() { return recursos; }
        public void setRecursos(Map<String, Double> recursos) { this.recursos = recursos; }
        public InfraestructuraDTO getInfraestructura() { return infraestructura; }
        public void setInfraestructura(InfraestructuraDTO infraestructura) { this.infraestructura = infraestructura; }
    }

    public static class InfraestructuraDTO {
        private int vivienda;
        private int industria;
        private TransporteDTO transporte;

        public int getVivienda() { return vivienda; }
        public void setVivienda(int vivienda) { this.vivienda = vivienda; }
        public int getIndustria() { return industria; }
        public void setIndustria(int industria) { this.industria = industria; }
        public TransporteDTO getTransporte() { return transporte; }
        public void setTransporte(TransporteDTO transporte) { this.transporte = transporte; }
    }

    public static class TransporteDTO {
        private boolean aereo;
        private boolean terrestre;
        private boolean naval;

        public boolean isAereo() { return aereo; }
        public void setAereo(boolean aereo) { this.aereo = aereo; }
        public boolean isTerrestre() { return terrestre; }
        public void setTerrestre(boolean terrestre) { this.terrestre = terrestre; }
        public boolean isNaval() { return naval; }
        public void setNaval(boolean naval) { this.naval = naval; }
    }

    public static class FederalDTO {
        private long poblacionTotal;
        private int bienestarFederal;
        private int presupuestoFederal;
        private int aguaPromedio;
        private int energiaPromedio;
        private int alimentoPromedio;
        private int saludPromedio;
        private int sostenibilidadPromedio;
        private int infraestructuraPromedio;
        private int desarrolloPromedio;
        private int distribucionPromedio;

        public long getPoblacionTotal() { return poblacionTotal; }
        public void setPoblacionTotal(long poblacionTotal) { this.poblacionTotal = poblacionTotal; }
        public int getBienestarFederal() { return bienestarFederal; }
        public void setBienestarFederal(int bienestarFederal) { this.bienestarFederal = bienestarFederal; }
        public int getPresupuestoFederal() { return presupuestoFederal; }
        public void setPresupuestoFederal(int presupuestoFederal) { this.presupuestoFederal = presupuestoFederal; }
        public int getAguaPromedio() { return aguaPromedio; }
        public void setAguaPromedio(int aguaPromedio) { this.aguaPromedio = aguaPromedio; }
        public int getEnergiaPromedio() { return energiaPromedio; }
        public void setEnergiaPromedio(int energiaPromedio) { this.energiaPromedio = energiaPromedio; }
        public int getAlimentoPromedio() { return alimentoPromedio; }
        public void setAlimentoPromedio(int alimentoPromedio) { this.alimentoPromedio = alimentoPromedio; }
        public int getSaludPromedio() { return saludPromedio; }
        public void setSaludPromedio(int saludPromedio) { this.saludPromedio = saludPromedio; }
        public int getSostenibilidadPromedio() { return sostenibilidadPromedio; }
        public void setSostenibilidadPromedio(int sostenibilidadPromedio) { this.sostenibilidadPromedio = sostenibilidadPromedio; }
        public int getInfraestructuraPromedio() { return infraestructuraPromedio; }
        public void setInfraestructuraPromedio(int infraestructuraPromedio) { this.infraestructuraPromedio = infraestructuraPromedio; }
        public int getDesarrolloPromedio() { return desarrolloPromedio; }
        public void setDesarrolloPromedio(int desarrolloPromedio) { this.desarrolloPromedio = desarrolloPromedio; }
        public int getDistribucionPromedio() { return distribucionPromedio; }
        public void setDistribucionPromedio(int distribucionPromedio) { this.distribucionPromedio = distribucionPromedio; }
    }
}
