package com.BiSourceMX.Proyect.controller;

import com.BiSourceMX.Proyect.dto.RecursoDTO;
import com.BiSourceMX.Proyect.dto.RecursoResponse;
import com.BiSourceMX.Proyect.service.RecursoService;
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
}
