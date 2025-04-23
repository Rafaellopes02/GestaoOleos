package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.MetodoPagamento;
import com.example.gestaooleos.API.repository.MetodoPagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MetodoPagamentoService {

    private final MetodoPagamentoRepository MetodoPagamentoRepository;

    public MetodoPagamentoService(MetodoPagamentoRepository metodoPagamentoRepository) {
        this.MetodoPagamentoRepository = metodoPagamentoRepository;
    }

    public Iterable<MetodoPagamento> listarMetodos() {
        return MetodoPagamentoRepository.findAll();
    }

    public Optional<MetodoPagamento> obterMetodoPorId(Long id) {
        return MetodoPagamentoRepository.findById(id);
    }

    public MetodoPagamento criarMetodo(MetodoPagamento metodo) {
        return MetodoPagamentoRepository.save(metodo);
    }

    public void removerMetodo(Long id) {
        MetodoPagamentoRepository.deleteById(id);
    }
}
