package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.PedidosContrato;
import com.example.gestaooleos.API.repository.PedidosContratoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidosContratoService {
    private final PedidosContratoRepository pedidosContratoRepository;

    public PedidosContratoService(PedidosContratoRepository pedidosContratoRepository) {
        this.pedidosContratoRepository = pedidosContratoRepository;
    }

    public Iterable<PedidosContrato> listarPedidosContrato() {
        return pedidosContratoRepository.findAll();
    }

    public Optional<PedidosContrato> obterPedidosContrato(Long id) {
        return pedidosContratoRepository.findById(id);
    }

    public PedidosContrato criarPedidosContrato(PedidosContrato pedidocontrato) {
        return pedidosContratoRepository.save(pedidocontrato);
    }

    public void removePedidosContrato(Long id) {
        pedidosContratoRepository.deleteById(id);
    }
}
