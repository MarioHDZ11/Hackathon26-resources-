package com.bisourcesmx.bisources.dto;

import java.util.List;
import java.util.Map;

public class GameConfigDTO {
    private long tickInterval;
    private double factorConsumo;
    private List<String> entities;
    private Map<String, InvestmentRule> investmentRules;

    public long getTickInterval() { return tickInterval; }
    public void setTickInterval(long tickInterval) { this.tickInterval = tickInterval; }
    public double getFactorConsumo() { return factorConsumo; }
    public void setFactorConsumo(double factorConsumo) { this.factorConsumo = factorConsumo; }
    public List<String> getEntities() { return entities; }
    public void setEntities(List<String> entities) { this.entities = entities; }
    public Map<String, InvestmentRule> getInvestmentRules() { return investmentRules; }
    public void setInvestmentRules(Map<String, InvestmentRule> investmentRules) { this.investmentRules = investmentRules; }

    public static class InvestmentRule {
        private Map<String, Integer> consume;
        private Map<String, Integer> genera;
        private Map<String, TransportMode> modos;

        public Map<String, Integer> getConsume() { return consume; }
        public void setConsume(Map<String, Integer> consume) { this.consume = consume; }
        public Map<String, Integer> getGenera() { return genera; }
        public void setGenera(Map<String, Integer> genera) { this.genera = genera; }
        public Map<String, TransportMode> getModos() { return modos; }
        public void setModos(Map<String, TransportMode> modos) { this.modos = modos; }
    }

    public static class TransportMode {
        private double costoPresupuesto;
        private double beneficio;

        public double getCostoPresupuesto() { return costoPresupuesto; }
        public void setCostoPresupuesto(double costoPresupuesto) { this.costoPresupuesto = costoPresupuesto; }
        public double getBeneficio() { return beneficio; }
        public void setBeneficio(double beneficio) { this.beneficio = beneficio; }
    }
}
