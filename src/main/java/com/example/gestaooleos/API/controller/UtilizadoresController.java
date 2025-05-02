package com.example.gestaooleos.API.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.service.UtilizadoresService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/Utilizadores")
public class UtilizadoresController {
    private final UtilizadoresService utilizadoresService;

    public UtilizadoresController(UtilizadoresService utilizadoresService) {
        this.utilizadoresService = utilizadoresService;
    }

    @GetMapping
    public Iterable<Utilizadores> listarUtilizadores() {
        return utilizadoresService.listarUtilizadores();
    }

    @GetMapping("/{id}")
    public Optional<Utilizadores> obterUtilizadores(@PathVariable Long id) {
        return utilizadoresService.obterUtilizadores(id);
    }

    @PostMapping
    public Utilizadores criarUtilizadores(@RequestBody Utilizadores utilizador) {
        return utilizadoresService.criarUtilizadores(utilizador);
    }

    @DeleteMapping("/{id}")
    public void removeUtilizadores(@PathVariable Long id) {
        utilizadoresService.removeUtilizadores(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Utilizadores utilizador) {
        return utilizadoresService.atualizarUtilizadores(id, utilizador)
                .map(u -> ResponseEntity.ok("Atualizado com sucesso"))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // AQUI adicionamos o novo endpoint
    @GetMapping("/clientes")
    public List<Utilizadores> listarClientes() {
        return (List<Utilizadores>) utilizadoresService.listarClientes();
    }
}
