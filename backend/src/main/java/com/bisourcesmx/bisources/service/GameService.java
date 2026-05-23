package com.bisourcesmx.bisources.service;

import com.bisourcesmx.bisources.dto.GameConfigDTO;
import com.bisourcesmx.bisources.dto.InitialGameStateDTO;
import com.bisourcesmx.bisources.model.Estado;
import com.bisourcesmx.bisources.repository.EstadoRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final EstadoRepository estadoRepository;
    private final EstadoService estadoService;

    private static final long FACTOR_CONSUMO = 15; // scaled (0.00000015 * 100000000 = 15)
    private static final long TICK_INTERVAL = 10000;

    public GameService(EstadoRepository estadoRepository, EstadoService estadoService) {
        this.estadoRepository = estadoRepository;
        this.estadoService = estadoService;
    }

    public GameConfigDTO getConfig() {
        GameConfigDTO config = new GameConfigDTO();
        config.setTickInterval(TICK_INTERVAL);
        config.setFactorConsumo(0.00000015);

        config.setEntities(List.of(
                "CDMX", "Jalisco", "Nuevo Leon", "Gobierno Federal",
                "ONU", "Cruz Roja", "Comision de Agua", "SEDENA"
        ));

        Map<String, GameConfigDTO.InvestmentRule> rules = new LinkedHashMap<>();

        rules.put("Agua", buildRule(30, 2, "Agua", 15, null));
        rules.put("Energia", buildRule(30, 3, "Energia", 15, null));
        rules.put("Alimento", buildRule(Map.of("Presupuesto", 40, "Sostenibilidad", 2, "Energia", 5, "Agua", 5), Map.of("Alimento", 15), null));
        rules.put("Salud", buildRule(Map.of("Presupuesto", 35, "Energia", 3, "Agua", 3), Map.of("Salud", 15), null));
        rules.put("Infraestructura", buildRule(Map.of("Presupuesto", 50, "Sostenibilidad", 3, "Energia", 5, "Agua", 5), Map.of("Presupuesto", 8), null));
        rules.put("D. Social y Cultural", buildRule(Map.of("Presupuesto", 30, "Sostenibilidad", 2, "Energia", 3, "Agua", 3), Map.of(), null));
        rules.put("Sostenibilidad", buildRule(Map.of("Presupuesto", 25, "Energia", 3, "Agua", 3), Map.of("Sostenibilidad", 15), null));
        rules.put("Distribucion", buildRule(30, 2, "Distribucion", 12, buildModos()));

        config.setInvestmentRules(rules);
        return config;
    }

    public InitialGameStateDTO getInitialState() {
        InitialGameStateDTO state = new InitialGameStateDTO();
        Map<String, InitialGameStateDTO.EstadoGameDTO> estados = new LinkedHashMap<>();

        List<Estado> dbEstados = estadoRepository.findAll();
        int counter = 0;
        long totalPoblacion = 0;
        double sumBienestar = 0;
        double sumAgua = 0, sumEnergia = 0, sumAlimento = 0, sumSalud = 0;
        double sumSostenibilidad = 0, sumInfra = 0, sumDesarrollo = 0, sumDistribucion = 0;

        for (Estado e : dbEstados) {
            Map<String, Double> recursos = generarValores(counter);
            int bienestar = calcBienestar(recursos);
            int presupuesto = 40 + ((counter * 17) % 51);
            boolean costero = e.getEsCostero() != null && e.getEsCostero();

            InitialGameStateDTO.EstadoGameDTO eg = new InitialGameStateDTO.EstadoGameDTO();
            eg.setNombre(e.getNombre());
            eg.setPoblacion(e.getPoblacionTotal() != null ? e.getPoblacionTotal() : 1000000);
            eg.setBienestar(bienestar);
            eg.setPresupuesto(presupuesto);
            eg.setRecursos(recursos);

            InitialGameStateDTO.InfraestructuraDTO inf = new InitialGameStateDTO.InfraestructuraDTO();
            inf.setVivienda(1);
            inf.setIndustria(1);
            InitialGameStateDTO.TransporteDTO trans = new InitialGameStateDTO.TransporteDTO();
            trans.setAereo(true);
            trans.setTerrestre(true);
            trans.setNaval(costero);
            inf.setTransporte(trans);
            eg.setInfraestructura(inf);

            String key = e.getCodigo3() != null ? e.getCodigo3() : String.valueOf(e.getId());
            estados.put(key, eg);

            totalPoblacion += eg.getPoblacion();
            sumBienestar += bienestar;
            sumAgua += recursos.get("agua");
            sumEnergia += recursos.get("energia");
            sumAlimento += recursos.get("alimento");
            sumSalud += recursos.get("salud");
            sumSostenibilidad += recursos.get("sostenibilidad");
            sumInfra += recursos.get("infraestructura");
            sumDesarrollo += recursos.get("desarrolloSociocultural");
            sumDistribucion += recursos.get("distribucion");
            counter++;
        }

        int total = counter > 0 ? counter : 1;
        InitialGameStateDTO.FederalDTO federal = new InitialGameStateDTO.FederalDTO();
        federal.setPoblacionTotal(totalPoblacion);
        federal.setBienestarFederal((int) Math.round(sumBienestar / total));
        federal.setPresupuestoFederal((int) Math.round(state.getEstados() != null ? state.getEstados().values().stream().mapToLong(InitialGameStateDTO.EstadoGameDTO::getPresupuesto).sum() : 0));
        federal.setAguaPromedio((int) Math.round(sumAgua / total));
        federal.setEnergiaPromedio((int) Math.round(sumEnergia / total));
        federal.setAlimentoPromedio((int) Math.round(sumAlimento / total));
        federal.setSaludPromedio((int) Math.round(sumSalud / total));
        federal.setSostenibilidadPromedio((int) Math.round(sumSostenibilidad / total));
        federal.setInfraestructuraPromedio((int) Math.round(sumInfra / total));
        federal.setDesarrolloPromedio((int) Math.round(sumDesarrollo / total));
        federal.setDistribucionPromedio((int) Math.round(sumDistribucion / total));

        state.setEstados(estados);
        state.setFederal(federal);

        federal.setPresupuestoFederal((int) Math.round(estados.values().stream().mapToLong(InitialGameStateDTO.EstadoGameDTO::getPresupuesto).sum()));

        return state;
    }

    private Map<String, Double> generarValores(int semilla) {
        int s = (semilla * 7 + 13) % 101;
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("agua", (double) (40 + ((s * 3) % 51)));
        map.put("energia", (double) (40 + ((s * 5) % 51)));
        map.put("alimento", (double) (40 + ((s * 7) % 51)));
        map.put("salud", (double) (35 + ((s * 11) % 51)));
        map.put("sostenibilidad", (double) (45 + ((s * 13) % 41)));
        map.put("infraestructura", (double) (25 + ((s * 17) % 46)));
        map.put("desarrolloSociocultural", (double) (30 + ((s * 19) % 46)));
        map.put("distribucion", (double) (20 + ((s * 23) % 46)));
        return map;
    }

    private int calcBienestar(Map<String, Double> recursos) {
        return (int) Math.round(recursos.values().stream().mapToDouble(Double::doubleValue).average().orElse(0));
    }

    private GameConfigDTO.InvestmentRule buildRule(int presupuesto, int sostenibilidad, String generaKey, int generaVal, Map<String, GameConfigDTO.TransportMode> modos) {
        return buildRule(
                Map.of("Presupuesto", presupuesto, "Sostenibilidad", sostenibilidad),
                Map.of(generaKey, generaVal),
                modos
        );
    }

    private GameConfigDTO.InvestmentRule buildRule(Map<String, Integer> consume, Map<String, Integer> genera, Map<String, GameConfigDTO.TransportMode> modos) {
        GameConfigDTO.InvestmentRule rule = new GameConfigDTO.InvestmentRule();
        rule.setConsume(consume);
        rule.setGenera(genera);
        rule.setModos(modos);
        return rule;
    }

    private Map<String, GameConfigDTO.TransportMode> buildModos() {
        Map<String, GameConfigDTO.TransportMode> modos = new LinkedHashMap<>();
        modos.put("Aereo", buildMode(1.5, 1.3));
        modos.put("Terrestre", buildMode(1.0, 1.0));
        modos.put("Naval", buildMode(0.7, 0.8));
        return modos;
    }

    private GameConfigDTO.TransportMode buildMode(double costo, double beneficio) {
        GameConfigDTO.TransportMode mode = new GameConfigDTO.TransportMode();
        mode.setCostoPresupuesto(costo);
        mode.setBeneficio(beneficio);
        return mode;
    }
}
