package com.example.gestaooleos.controller;


import com.example.gestaooleos.model.EstadosRecolhas;
import com.example.gestaooleos.service.EstadosRecolhasService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/EstadosRecolhas")
public class EstadosRecolhasController {
    private final EstadosRecolhasService estadosRecolhasService;

    public EstadosRecolhasController(EstadosRecolhasService estadosRecolhasService) {
        this.estadosRecolhasService = estadosRecolhasService;
    }

    @GetMapping
    public Iterable<EstadosRecolhas> listarEstadosRecolhas() {
        return estadosRecolhasService.listarEstadosRecolhas();
    }

    @GetMapping("/{id}")
    public Optional<EstadosRecolhas> obterEstadosRecolhas(@PathVariable Long id) {
        return estadosRecolhasService.obterEstadosRecolhas(id);
    }

    @PostMapping
    public EstadosRecolhas criarEstadoRecolhas(@RequestBody EstadosRecolhas estadosRecolhas) {
        return estadosRecolhasService.criarEstadoRecolha(estadosRecolhas);
    }

    @DeleteMapping("/{id}")
    public void removeEstadosRecolhas(@PathVariable Long id) {
        estadosRecolhasService.removeEstadosRecolhas(id);
    }
}
