package com.BiSourceMX.Proyect.controller;

import com.BiSourceMX.Proyect.dto.EstadoDTO;
import com.BiSourceMX.Proyect.dto.EstadoResponse;
import com.BiSourceMX.Proyect.service.EstadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping
    public ResponseEntity<List<EstadoDTO>> getAll() {
        return ResponseEntity.ok(estadoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoService.getById(id));
    }
}
