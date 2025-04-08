package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.EstadosPedidos;
import com.example.gestaooleos.API.repository.EstadosPedidosRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EstadosPedidosService {
    private final EstadosPedidosRepository estadosPedidosRepository;

    public EstadosPedidosService(EstadosPedidosRepository estadosPedidosRepository) {
        this.estadosPedidosRepository = estadosPedidosRepository;
    }

    public Iterable<EstadosPedidos> listarEstadosPedidos() {
        return estadosPedidosRepository.findAll();
    }

    public Optional<EstadosPedidos> obterEstadosPedidos(Long id) {
        return estadosPedidosRepository.findById(id);
    }

    public EstadosPedidos criarEstadoPedido(EstadosPedidos estadosPedido) {
        return estadosPedidosRepository.save(estadosPedido);
    }

    public void removeEstadosPedidos(Long id) {
        estadosPedidosRepository.deleteById(id);
    }
}
