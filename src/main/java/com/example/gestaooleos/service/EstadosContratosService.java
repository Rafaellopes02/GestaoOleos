package com.example.gestaooleos.service;

import com.example.gestaooleos.model.EstadosContratos;
import com.example.gestaooleos.repository.EstadosContratosRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstadosContratosService {
    private final EstadosContratosRepository estadosContratosRepository;

    public EstadosContratosService(EstadosContratosRepository estadosContratosRepository) {
        this.estadosContratosRepository = estadosContratosRepository;
    }

    public Iterable<EstadosContratos> listarEstadosContratos() {
        return estadosContratosRepository.findAll();
    }

    public Optional<EstadosContratos> obterEstadosContratos(Long id) {
        return estadosContratosRepository.findById(id);
    }

    public EstadosContratos criarEstadoContratos(EstadosContratos estadosContratos) {
        return estadosContratosRepository.save(estadosContratos);
    }

    public void removeEstadoContratos(Long id) {
        estadosContratosRepository.deleteById(id);
    }
}
