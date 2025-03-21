package com.example.gestaooleos.service;

import com.example.gestaooleos.model.Utilizadores;
import com.example.gestaooleos.repository.UtilizadoresRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilizadoresService {
    private final UtilizadoresRepository utilizadoresRepository;

    public UtilizadoresService(UtilizadoresRepository utilizadoresRepository) {
        this.utilizadoresRepository = utilizadoresRepository;
    }

    public Iterable<Utilizadores> listarUtilizadores() {
        return utilizadoresRepository.findAll();
    }

    public Optional<Utilizadores> obterUtilizadores(Long id) {
        return utilizadoresRepository.findById(id);
    }

    public Utilizadores criarUtilizadores(Utilizadores utilizador) {
        return utilizadoresRepository.save(utilizador);
    }

    public void removeUtilizadores(Long id) {
        utilizadoresRepository.deleteById(id);
    }
}
