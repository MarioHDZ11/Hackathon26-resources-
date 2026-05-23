package com.BiSourceMX.Proyect.controller;

import com.BiSourceMX.Proyect.dto.PartidaDTO;
import com.BiSourceMX.Proyect.service.PartidaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidas")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @GetMapping("/ultima")
    public ResponseEntity<PartidaDTO> getUltimaActiva() {
        PartidaDTO dto = partidaService.getUltimaActiva();
        if (dto == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(partidaService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PartidaDTO> crear(@RequestBody PartidaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partidaService.crearNueva(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartidaDTO> actualizar(@PathVariable Integer id, @RequestBody PartidaDTO dto) {
        return ResponseEntity.ok(partidaService.actualizarPartida(id, dto));
    }
}
