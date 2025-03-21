package com.example.gestaooleos.controller;


import com.example.gestaooleos.model.EstadosPedidos;
import com.example.gestaooleos.service.EstadosPedidosService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/EstadosPedidos")
public class EstadosPedidosController {
    private final EstadosPedidosService estadosPedidosService;

    public EstadosPedidosController(EstadosPedidosService estadosPedidosService) {
        this.estadosPedidosService = estadosPedidosService;
    }

    @GetMapping
    public Iterable<EstadosPedidos> listarEstadosPedidos() {
        return estadosPedidosService.listarEstadosPedidos();
    }

    @GetMapping("/{id}")
    public Optional<EstadosPedidos> obterEstadosPedidos(@PathVariable Long id) {
        return estadosPedidosService.obterEstadosPedidos(id);
    }

    @PostMapping
    public EstadosPedidos criarEstadoPedidos(@RequestBody EstadosPedidos estadosPedidos) {
        return estadosPedidosService.criarEstadoPedido(estadosPedidos);
    }

    @DeleteMapping("/{id}")
    public void removeEstadosPedidos(@PathVariable Long id) {
        estadosPedidosService.removeEstadosPedidos(id);
    }
}
