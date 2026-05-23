package com.bisourcesmx.bisources.controller;

import com.bisourcesmx.bisources.dto.EstadoDTO;
import com.bisourcesmx.bisources.dto.MunicipioDTO;
import com.bisourcesmx.bisources.service.EstadoService;
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
    public ResponseEntity<List<EstadoDTO>> getAllEstados() {
        return ResponseEntity.ok(estadoService.getAllEstados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoDTO> getEstado(@PathVariable String id) {
        EstadoDTO dto = null;
        try {
            dto = estadoService.getEstadoById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            dto = estadoService.getEstadoByCodigo3(id);
        }
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/municipios")
    public ResponseEntity<List<MunicipioDTO>> getMunicipios(@PathVariable String id) {
        EstadoDTO estado = null;
        try {
            estado = estadoService.getEstadoById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            estado = estadoService.getEstadoByCodigo3(id);
        }
        if (estado == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(estadoService.getMunicipiosByEstadoNombre(estado.getName()));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EstadoDTO> getEstadoByNombre(@PathVariable String nombre) {
        EstadoDTO dto = estadoService.getEstadoByNombre(nombre);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/nombre/{nombre}/municipios")
    public ResponseEntity<List<MunicipioDTO>> getMunicipiosByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(estadoService.getMunicipiosByEstadoNombre(nombre));
    }
}
