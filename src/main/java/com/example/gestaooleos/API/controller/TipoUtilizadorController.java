package com.example.gestaooleos.API.controller;


import com.example.gestaooleos.API.model.TipoUtilizador;
import com.example.gestaooleos.API.service.TipoUtilizadorService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/TipoUtilizador")
public class TipoUtilizadorController {
    private final TipoUtilizadorService tipoUtilizadorService;

    public TipoUtilizadorController(TipoUtilizadorService tipoUtilizadorService) {
        this.tipoUtilizadorService = tipoUtilizadorService;
    }

    @GetMapping
    public Iterable<TipoUtilizador> listarTipoUtilizador() {
        return tipoUtilizadorService.listarTipoUtilizador();
    }

    @GetMapping("/{id}")
    public Optional<TipoUtilizador> obterTipoUtilizador(@PathVariable Long id) {
        return tipoUtilizadorService.obterTipoUtilizador(id);
    }

    @PostMapping
    public TipoUtilizador criarTipoUtilizador(@RequestBody TipoUtilizador tipoutilizador) {
        return tipoUtilizadorService.criarTipoUtilizador(tipoutilizador);
    }

    @DeleteMapping("/{id}")
    public void removeTipoUtilizador(@PathVariable Long id) {
        tipoUtilizadorService.removeTipoUtilizador(id);
    }
}
