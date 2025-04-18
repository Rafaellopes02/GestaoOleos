package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.repository.ContratosRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContratosService {
    private final ContratosRepository contratosRepository;

    public ContratosService(ContratosRepository contratosRepository) {
        this.contratosRepository = contratosRepository;
    }

    public Iterable<Contratos> listarContratos() {
        return contratosRepository.findAll();
    }

    public Optional<Contratos> obterContratos(Long id) {
        return contratosRepository.findById(id);
    }

    public Contratos criarContratos(Contratos contrato) {
        return contratosRepository.save(contrato);
    }

    public void removeContratos(Long id) {
        contratosRepository.deleteById(id);
    }
}
