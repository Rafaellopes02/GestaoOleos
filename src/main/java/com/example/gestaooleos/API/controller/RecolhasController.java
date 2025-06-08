package com.example.gestaooleos.API.controller;

import java.util.Map;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.gestaooleos.API.model.Recolhas;
import com.example.gestaooleos.API.service.RecolhasService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Recolhas")
public class RecolhasController {
    private final RecolhasService recolhasService;

    public RecolhasController(RecolhasService recolhasService) {
        this.recolhasService = recolhasService;
    }

    @GetMapping
    public Iterable<Recolhas> listarRecolhas() {
        return recolhasService.listarRecolhas();
    }

    @GetMapping("/{id}")
    public Optional<Recolhas> obterRecolhas(@PathVariable Long id) {
        return recolhasService.obterRecolhas(id);
    }

    @PostMapping
    public Recolhas criarRecolhas(@RequestBody Recolhas recolha) {
        return recolhasService.criarRecolhas(recolha);
    }

    @DeleteMapping("/{id}")
    public void removeRecolhas(@PathVariable Long id) {
        recolhasService.removeRecolhas(id);
    }

    @PatchMapping("/{id}/estado/{novoEstadoId}")
    public void atualizarEstadoRecolha(@PathVariable Long id, @PathVariable int novoEstadoId) {
        recolhasService.atualizarEstado(id, novoEstadoId);
    }

    @GetMapping("/contar")
    public ResponseEntity<Map<String, Integer>> contarRecolhasPorEstado(@RequestParam int estado) {
        int total = recolhasService.contarPorEstado(estado);
        Map<String, Integer> resposta = new HashMap<>();
        resposta.put("quantidade", total);
        return ResponseEntity.ok(resposta);
    }

}