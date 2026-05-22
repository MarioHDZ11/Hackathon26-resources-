package com.BiSourceMX.Proyect.controller;

import com.BiSourceMX.Proyect.dto.EstadoDTO;
import com.BiSourceMX.Proyect.dto.EstadoResponse;
import com.BiSourceMX.Proyect.service.EstadoService;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<EstadoDTO> create(@RequestBody EstadoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoDTO> update(@PathVariable Integer id, @RequestBody EstadoDTO dto) {
        return ResponseEntity.ok(estadoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        estadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
