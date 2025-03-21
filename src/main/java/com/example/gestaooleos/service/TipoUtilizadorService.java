package com.example.gestaooleos.service;

import com.example.gestaooleos.model.TipoUtilizador;
import com.example.gestaooleos.repository.TipoUtilizadorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TipoUtilizadorService {
    private final TipoUtilizadorRepository tipoUtilizadorRepository;

    public TipoUtilizadorService(TipoUtilizadorRepository tipoUtilizadorRepository) {
        this.tipoUtilizadorRepository = tipoUtilizadorRepository;
    }

    public Iterable<TipoUtilizador> listarTipoUtilizador() {
        return tipoUtilizadorRepository.findAll();
    }

    public Optional<TipoUtilizador> obterTipoUtilizador(Long id) {
        return tipoUtilizadorRepository.findById(id);
    }

    public TipoUtilizador criarTipoUtilizador(TipoUtilizador tipoutilizador) {
        return tipoUtilizadorRepository.save(tipoutilizador);
    }

    public void removeTipoUtilizador(Long id) {
        tipoUtilizadorRepository.deleteById(id);
    }
}
