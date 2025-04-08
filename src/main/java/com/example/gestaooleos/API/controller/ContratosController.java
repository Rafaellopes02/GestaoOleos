package com.example.gestaooleos.API.controller;


import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.service.ContratosService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Contratos")
public class ContratosController {
    private final ContratosService contratosService;

    public ContratosController(ContratosService contratosService) {
        this.contratosService = contratosService;
    }

    @GetMapping
    public Iterable<Contratos> listarContratos() {
        return contratosService.listarContratos();
    }

    @GetMapping("/{id}")
    public Optional<Contratos> obterContratos(@PathVariable Long id) {
        return contratosService.obterContratos(id);
    }

    @PostMapping
    public Contratos criarContratos(@RequestBody Contratos contratos) {
        return contratosService.criarContratos(contratos);
    }

    @DeleteMapping("/{id}")
    public void removeContratos(@PathVariable Long id) {
        contratosService.removeContratos(id);
    }
}
