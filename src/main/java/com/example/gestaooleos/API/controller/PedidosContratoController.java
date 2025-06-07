package com.example.gestaooleos.API.controller;


import com.example.gestaooleos.API.dto.PedidoContratoDetalhadoDTO;
import com.example.gestaooleos.API.model.PedidosContrato;
import com.example.gestaooleos.API.service.PedidosContratoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @GetMapping("/pendentes/count")
    public long contarPedidosPendentes() {
        return pedidosContratoService.contarPedidosPendentes();
    }

    @GetMapping("/detalhes")
    public List<PedidoContratoDetalhadoDTO> listarDetalhes() {
        return pedidosContratoService.listarPedidosComDetalhes();
    }

    @PatchMapping("/{id}")
    public void atualizarEstados(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        int estadoPedido = payload.get("idestadopedido");
        int estadoContrato = payload.get("idestadocontrato");
        pedidosContratoService.atualizarEstadoPedido(id, estadoPedido, estadoContrato);
    }

}
