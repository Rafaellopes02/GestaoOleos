package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.repository.UtilizadoresRepository;
import com.example.gestaooleos.API.dto.UtilizadorDTO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public Map<String, Long> contarPorTipo() {
        Map<Integer, String> mapa = Map.of(
                1, "Clientes",
                2, "Empregados",
                3, "Escritórios",
                4, "Comerciais"
        );

        Iterable<Utilizadores> iterable = utilizadoresRepository.findAll();

        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.groupingBy(
                        u -> mapa.getOrDefault(u.getIdtipoutilizador(), "Outro"),
                        Collectors.counting()
                ));
    }

    public List<UtilizadorDTO> buscarEmpregados() {
        List<Utilizadores> empregados = utilizadoresRepository.findByIdtipoutilizador(2); // tipo 2 = empregado
        return empregados.stream()
                .map(emp -> new UtilizadorDTO(emp.getIdutilizador(), emp.getNome()))
                .collect(Collectors.toList());
    }


}
