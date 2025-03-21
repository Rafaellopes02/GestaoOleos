package com.example.gestaooleos.controller;


import com.example.gestaooleos.model.Recolhas;
import com.example.gestaooleos.service.RecolhasService;
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
}
