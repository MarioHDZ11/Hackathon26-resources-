package com.BiSourceMX.Proyect.controller;

import com.BiSourceMX.Proyect.dto.RecursoEstadoDTO;
import com.BiSourceMX.Proyect.service.RecursoEstadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relaciones")
public class RecursoEstadoController {

    private final RecursoEstadoService recursoEstadoService;

    public RecursoEstadoController(RecursoEstadoService recursoEstadoService) {
        this.recursoEstadoService = recursoEstadoService;
    }

    @GetMapping("/estado/{idEstado}")
    public ResponseEntity<List<RecursoEstadoDTO>> getByEstado(@PathVariable Integer idEstado) {
        return ResponseEntity.ok(recursoEstadoService.getByEstado(idEstado));
    }

    @GetMapping("/recurso/{idRecurso}")
    public ResponseEntity<List<RecursoEstadoDTO>> getByRecurso(@PathVariable Integer idRecurso) {
        return ResponseEntity.ok(recursoEstadoService.getByRecurso(idRecurso));
    }

    @PostMapping("/estado/{idEstado}/recurso/{idRecurso}")
    public ResponseEntity<RecursoEstadoDTO> associate(
            @PathVariable Integer idEstado,
            @PathVariable Integer idRecurso,
            @RequestBody RecursoEstadoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recursoEstadoService.associate(idEstado, idRecurso, dto));
    }

    @PutMapping("/estado/{idEstado}/recurso/{idRecurso}")
    public ResponseEntity<RecursoEstadoDTO> update(
            @PathVariable Integer idEstado,
            @PathVariable Integer idRecurso,
            @RequestBody RecursoEstadoDTO dto) {
        return ResponseEntity.ok(recursoEstadoService.update(idEstado, idRecurso, dto));
    }

    @DeleteMapping("/estado/{idEstado}/recurso/{idRecurso}")
    public ResponseEntity<Void> disassociate(
            @PathVariable Integer idEstado,
            @PathVariable Integer idRecurso) {
        recursoEstadoService.disassociate(idEstado, idRecurso);
        return ResponseEntity.noContent().build();
    }
}
