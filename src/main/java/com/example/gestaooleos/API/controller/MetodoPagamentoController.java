package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.model.MetodoPagamento;
import com.example.gestaooleos.API.service.MetodoPagamentoService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/metodospagamento")
public class MetodoPagamentoController {

    private final MetodoPagamentoService metodoPagamentoService;

    public MetodoPagamentoController(MetodoPagamentoService metodoPagamentoService) {
        this.metodoPagamentoService = metodoPagamentoService;
    }

    @GetMapping
    public Iterable<MetodoPagamento> listarMetodos() {
        return metodoPagamentoService.listarMetodos();
    }

    @GetMapping("/{id}")
    public Optional<MetodoPagamento> obterMetodo(@PathVariable Long id) {
        return metodoPagamentoService.obterMetodoPorId(id);
    }

    @PostMapping
    public MetodoPagamento criarMetodo(@RequestBody MetodoPagamento metodoPagamento) {
        return metodoPagamentoService.criarMetodo(metodoPagamento);
    }

    @DeleteMapping("/{id}")
    public void apagarMetodo(@PathVariable Long id) {
        metodoPagamentoService.removerMetodo(id);
    }
}
