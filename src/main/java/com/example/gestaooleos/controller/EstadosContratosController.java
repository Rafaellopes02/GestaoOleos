package com.example.gestaooleos.controller;


import com.example.gestaooleos.model.EstadosContratos;
import com.example.gestaooleos.service.EstadosContratosService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/EstadosContratos")
public class EstadosContratosController {
    private final EstadosContratosService estadosContratosService;

    public EstadosContratosController(EstadosContratosService estadosContratosService) {
        this.estadosContratosService = estadosContratosService;
    }

    @GetMapping
    public Iterable<EstadosContratos> listarEstadosContratos() {
        return estadosContratosService.listarEstadosContratos();
    }

    @GetMapping("/{id}")
    public Optional<EstadosContratos> obterEstadosContratos(@PathVariable Long id) {
        return estadosContratosService.obterEstadosContratos(id);
    }

    @PostMapping
    public EstadosContratos criarEstadoContratos(@RequestBody EstadosContratos estadosContratos) {
        return estadosContratosService.criarEstadoContratos(estadosContratos);
    }

    @DeleteMapping("/{id}")
    public void removeEstadosContratos(@PathVariable Long id) {
        estadosContratosService.removeEstadoContratos(id);
    }
}
