package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.EstadosRecolhas;
import com.example.gestaooleos.API.repository.EstadosRecolhasRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstadosRecolhasService {
    private final EstadosRecolhasRepository estadosRecolhasRepository;

    public EstadosRecolhasService(EstadosRecolhasRepository estadosRecolhasRepository) {
        this.estadosRecolhasRepository = estadosRecolhasRepository;
    }

    public Iterable<EstadosRecolhas> listarEstadosRecolhas() {
        return estadosRecolhasRepository.findAll();
    }

    public Optional<EstadosRecolhas> obterEstadosRecolhas(Long id) {
        return estadosRecolhasRepository.findById(id);
    }

    public EstadosRecolhas criarEstadoRecolha(EstadosRecolhas estadosRecolha) {
        return estadosRecolhasRepository.save(estadosRecolha);
    }

    public void removeEstadosRecolhas(Long id) {
        estadosRecolhasRepository.deleteById(id);
    }
}
