package com.example.gestaooleos.API.controller;


import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.service.UtilizadoresService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Utilizadores")
public class UtilizadoresController {
    private final UtilizadoresService utilizadoresService;

    public UtilizadoresController(UtilizadoresService utilizadoresService) {
        this.utilizadoresService = utilizadoresService;
    }

    @GetMapping
    public Iterable<Utilizadores> listarUtilizadores() {
        return utilizadoresService.listarUtilizadores();
    }

    @GetMapping("/{id}")
    public Optional<Utilizadores> obterUtilizadores(@PathVariable Long id) {
        return utilizadoresService.obterUtilizadores(id);
    }

    @PostMapping
    public Utilizadores criarUtilizadores(@RequestBody Utilizadores utilizador) {
        return utilizadoresService.criarUtilizadores(utilizador);
    }

    @DeleteMapping("/{id}")
    public void removeUtilizadores(@PathVariable Long id) {
        utilizadoresService.removeUtilizadores(id);
    }
}
