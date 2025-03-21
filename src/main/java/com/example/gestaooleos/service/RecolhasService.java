package com.example.gestaooleos.service;

import com.example.gestaooleos.model.Recolhas;
import com.example.gestaooleos.repository.RecolhasRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecolhasService {
    private final RecolhasRepository recolhasRepository;

    public RecolhasService(RecolhasRepository recolhasRepository) {
        this.recolhasRepository = recolhasRepository;
    }

    public Iterable<Recolhas> listarRecolhas() {
        return recolhasRepository.findAll();
    }

    public Optional<Recolhas> obterRecolhas(Long id) {
        return recolhasRepository.findById(id);
    }

    public Recolhas criarRecolhas(Recolhas recolha) {
        return recolhasRepository.save(recolha);
    }

    public void removeRecolhas(Long id) {
        recolhasRepository.deleteById(id);
    }
}
