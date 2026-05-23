package com.bisourcesmx.bisources.controller;

import com.bisourcesmx.bisources.dto.GameConfigDTO;
import com.bisourcesmx.bisources.dto.InitialGameStateDTO;
import com.bisourcesmx.bisources.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/config")
    public ResponseEntity<GameConfigDTO> getConfig() {
        return ResponseEntity.ok(gameService.getConfig());
    }

    @GetMapping("/initial")
    public ResponseEntity<InitialGameStateDTO> getInitialState() {
        return ResponseEntity.ok(gameService.getInitialState());
    }
}
