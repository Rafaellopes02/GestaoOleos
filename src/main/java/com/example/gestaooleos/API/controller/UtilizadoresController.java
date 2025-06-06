package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.dto.LoginRequest;
import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.security.JwtUtil;
import com.example.gestaooleos.API.service.UtilizadoresService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Utilizadores")
public class UtilizadoresController {
    private final UtilizadoresService utilizadoresService;
    private final JwtUtil jwtUtil;

    public UtilizadoresController(UtilizadoresService utilizadoresService, JwtUtil jwtUtil) {
        this.utilizadoresService = utilizadoresService;
        this.jwtUtil = jwtUtil;
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

    @GetMapping("/clientes")
    public List<Utilizadores> listarClientes() {
        return (List<Utilizadores>) utilizadoresService.listarClientes();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<Utilizadores> utilizadorOpt = utilizadoresService.encontrarPorUsername(request.getUsername());

            if (utilizadorOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Utilizador não encontrado");
            }

            Utilizadores utilizador = utilizadorOpt.get();

            if (!utilizador.getPassword().equals(request.getPassword())) {
                return ResponseEntity.status(401).body("Palavra-passe incorreta");
            }

            String token = jwtUtil.generateToken(utilizador.getIdutilizador(), utilizador.getIdtipoutilizador());

            return ResponseEntity.ok().body(Map.of("token", token));
        } catch (Exception e) {
            e.printStackTrace(); // Mostra erro detalhado no terminal
            return ResponseEntity.status(500).body("Erro interno no servidor: " + e.getMessage());
        }
    }

    @GetMapping("/contar-por-tipo")
    public Map<String, Long> contarPorTipoUtilizador() {
        return utilizadoresService.contarPorTipo();
    }
}
