package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.repository.UtilizadoresRepository;
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

    public Optional<Utilizadores> atualizarUtilizadores(Long id, Utilizadores novosDados) {
        return utilizadoresRepository.findById(id).map(existente -> {
            existente.setNome(novosDados.getNome());
            existente.setTelefone(novosDados.getTelefone());
            existente.setMorada(novosDados.getMorada());
            existente.setUsername(novosDados.getUsername());
            existente.setIdtipoutilizador(novosDados.getIdtipoutilizador());
            // Se tiveres password ou outros campos, adiciona aqui

            return utilizadoresRepository.save(existente);
        });
    }

    public Iterable<Utilizadores> listarClientes() {
        return utilizadoresRepository.findClientes();
    }

    public Optional<Utilizadores> encontrarPorUsername(String username) {
        return utilizadoresRepository.findByUsername(username);
    }
}
