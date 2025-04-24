package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.model.EstadosPagamento;
import com.example.gestaooleos.API.service.EstadosPagamentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadospagamento")
public class EstadosPagamentoController {

    private final EstadosPagamentoService service;

    public EstadosPagamentoController(EstadosPagamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EstadosPagamento> listarTodos() {
        return service.listarEstados();
    }

    @PostMapping
    public EstadosPagamento criar(@RequestBody EstadosPagamento estado) {
        return service.criarEstado(estado);
    }

    @DeleteMapping("/{id}")
    public void apagar(@PathVariable Long id) {
        service.apagarEstado(id);
    }

    @GetMapping("/{id}")
    public EstadosPagamento obter(@PathVariable Long id) {
        return service.obterEstado(id);
    }
}
