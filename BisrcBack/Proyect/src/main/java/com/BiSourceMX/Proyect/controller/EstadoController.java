package com.BiSourceMX.Proyect.controller;

import com.BiSourceMX.Proyect.dto.EstadoDTO;
import com.BiSourceMX.Proyect.dto.EstadoInfoDTO;
import com.BiSourceMX.Proyect.dto.EstadoResponse;
import com.BiSourceMX.Proyect.service.EstadoDataService;
import com.BiSourceMX.Proyect.service.EstadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    private final EstadoService estadoService;
    private final EstadoDataService estadoDataService;

    public EstadoController(EstadoService estadoService, EstadoDataService estadoDataService) {
        this.estadoService = estadoService;
        this.estadoDataService = estadoDataService;
    }

    @GetMapping
    public ResponseEntity<List<EstadoDTO>> getAll() {
        return ResponseEntity.ok(estadoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoService.getById(id));
    }

    @GetMapping("/data")
    public ResponseEntity<List<EstadoInfoDTO>> getAllData(
            @RequestParam(required = false) String region) {
        if (region != null && !region.isBlank()) {
            return ResponseEntity.ok(estadoDataService.filtrarPorRegion(region));
        }
        return ResponseEntity.ok(estadoDataService.obtenerTodos());
    }

    @GetMapping("/data/nombre/{nombre}")
    public ResponseEntity<EstadoInfoDTO> getByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(estadoDataService.obtenerPorNombre(nombre));
    }

    @GetMapping("/data/filtrar")
    public ResponseEntity<List<EstadoInfoDTO>> filtrarPorRecurso(
            @RequestParam String recurso) {
        return ResponseEntity.ok(estadoDataService.filtrarPorIntercambioAlto(recurso));
    }
}
