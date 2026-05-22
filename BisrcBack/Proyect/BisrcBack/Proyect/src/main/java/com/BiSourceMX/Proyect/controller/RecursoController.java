package com.BiSourceMX.Proyect.controller;

import com.BiSourceMX.Proyect.dto.RecursoDTO;
import com.BiSourceMX.Proyect.dto.RecursoResponse;
import com.BiSourceMX.Proyect.service.RecursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final RecursoService recursoService;

    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    @GetMapping
    public ResponseEntity<List<RecursoDTO>> getAll() {
        return ResponseEntity.ok(recursoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(recursoService.getById(id));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RecursoDTO>> getByCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(recursoService.getByCategoria(categoria));
    }

    @PostMapping
    public ResponseEntity<RecursoDTO> create(@RequestBody RecursoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(recursoService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoDTO> update(@PathVariable Integer id, @RequestBody RecursoDTO dto) {
        return ResponseEntity.ok(recursoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        recursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
