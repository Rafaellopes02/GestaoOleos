package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.dto.RecolhaDTOBackend;
import com.example.gestaooleos.API.mapper.RecolhaMapper;
import com.example.gestaooleos.API.model.Recolhas;
import com.example.gestaooleos.API.repository.RecolhasRepository;
import com.example.gestaooleos.API.service.RecolhasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/recolhas")
public class RecolhasController {

    private final RecolhasService recolhasService;

    @Autowired
    private RecolhasRepository recolhasRepository;

    public RecolhasController(RecolhasService recolhasService) {
        this.recolhasService = recolhasService;
    }

    @GetMapping
    public Iterable<Recolhas> listarRecolhas() {
        return recolhasService.listarRecolhas();
    }

    @GetMapping("/{id}")
    public Optional<Recolhas> obterRecolhas(@PathVariable Long id) {
        return recolhasService.obterRecolhas(id);
    }

    @PostMapping
    public Recolhas criarRecolhas(@RequestBody Recolhas recolha) {
        return recolhasService.criarRecolhas(recolha);
    }

    @DeleteMapping("/{id}")
    public void removerRecolhas(@PathVariable Long id) {
        recolhasService.removeRecolhas(id);
    }

    @PatchMapping("/{id}/estado/{novoEstadoId}")
    public void atualizarEstadoRecolha(@PathVariable Long id, @PathVariable int novoEstadoId) {
        recolhasService.atualizarEstado(id, novoEstadoId);
    }

    @GetMapping("/contar")
    public ResponseEntity<Map<String, Integer>> contarRecolhasPorEstado(@RequestParam int estado) {
        int total = recolhasService.contarPorEstado(estado);
        Map<String, Integer> resposta = new HashMap<>();
        resposta.put("quantidade", total);
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/em-andamento")
    public List<Recolhas> listarRecolhasEmAndamento() {
        return recolhasService.listarPorEstado(2); // Estado 2 = Em andamento
    }

    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<RecolhaDTOBackend>> listarRecolhasFuncionario(@PathVariable Long id) {
        System.out.println("📥 Pedido de recolhas para funcionário ID: " + id);
        List<Recolhas> lista = recolhasRepository.findByIdutilizadorAndIdestadorecolha(id, 5);
        System.out.println("🔎 Recolhas encontradas: " + lista.size());

        List<RecolhaDTOBackend> dtos = lista.stream()
                .map(RecolhaMapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/observacoes")
    public ResponseEntity<?> atualizarObservacoes(@PathVariable Long id, @RequestBody String obs) {
        recolhasService.atualizarObservacoes(id, obs);
        return ResponseEntity.ok().build();
    }




    @PatchMapping("/{id}/notificar/{estado}/{empregado}")
    public ResponseEntity<?> notificarEmpregado(
            @PathVariable Long id,
            @PathVariable int estado,
            @PathVariable Long empregado) {
        recolhasService.notificarEmpregado(id, estado, empregado);
        return ResponseEntity.ok().build();
    }


}
