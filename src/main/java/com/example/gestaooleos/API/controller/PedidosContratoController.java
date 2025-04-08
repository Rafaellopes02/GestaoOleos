package com.example.gestaooleos.API.controller;


import com.example.gestaooleos.API.model.PedidosContrato;
import com.example.gestaooleos.API.service.PedidosContratoService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/PedidosContrato")
public class PedidosContratoController {
    private final PedidosContratoService pedidosContratoService;

    public PedidosContratoController(PedidosContratoService pedidosContratoService) {
        this.pedidosContratoService = pedidosContratoService;
    }

    @GetMapping
    public Iterable<PedidosContrato> listarNotificacoes() {
        return pedidosContratoService.listarPedidosContrato();
    }

    @GetMapping("/{id}")
    public Optional<PedidosContrato> obterNotificacoes(@PathVariable Long id) {
        return pedidosContratoService.obterPedidosContrato(id);
    }

    @PostMapping
    public PedidosContrato criarNotificacoes(@RequestBody PedidosContrato pedidocontrato) {
        return pedidosContratoService.criarPedidosContrato(pedidocontrato);
    }

    @DeleteMapping("/{id}")
    public void removePedidosContrato(@PathVariable Long id) {
        pedidosContratoService.removePedidosContrato(id);
    }
}
