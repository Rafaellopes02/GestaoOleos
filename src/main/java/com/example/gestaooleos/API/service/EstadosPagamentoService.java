package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.EstadosPagamento;
import com.example.gestaooleos.API.repository.EstadosPagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EstadosPagamentoService {
    private final EstadosPagamentoRepository repository;

    public EstadosPagamentoService(EstadosPagamentoRepository repository) {
        this.repository = repository;
    }

    public List<EstadosPagamento> listarEstados() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public EstadosPagamento criarEstado(EstadosPagamento estado) {
        return repository.save(estado);
    }

    public void apagarEstado(Long id) {
        repository.deleteById(id);
    }

    public EstadosPagamento obterEstado(Long id) {
        return repository.findById(id).orElse(null);
    }
}
