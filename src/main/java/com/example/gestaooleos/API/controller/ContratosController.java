package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.dto.*;
import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.service.ContratosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Contratos")
public class ContratosController {
    private final ContratosService contratosService;

    public ContratosController(ContratosService contratosService) {
        this.contratosService = contratosService;
    }

    // ✅ Este vem antes de {id}
    @GetMapping("/contar-estados")
    public Map<String, Long> contarContratosPorEstado() {
        return contratosService.contarContratosPorEstado();
    }

    @GetMapping
    public Iterable<Contratos> listarContratos() {
        return contratosService.listarContratos();
    }

    @GetMapping("/{id}")
    public Optional<Contratos> obterContratos(@PathVariable Long id) {
        return contratosService.obterContratos(id);
    }

    @PostMapping
    public Contratos criarContratos(@RequestBody ContratoDTOBackend contratoDTOBackend) {
        try {
            return contratosService.criarContratoComPagamento(contratoDTOBackend);
        } catch (Exception ex) {
            ex.printStackTrace(); // Mostra o erro completo na consola do servidor
            throw new RuntimeException("Erro ao criar contrato: " + ex.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public void removeContratos(@PathVariable Long id) {
        contratosService.removeContratos(id);
    }

    @GetMapping("/com-estado")
    public List<ContratoDTOBackend> listarContratosComEstado() {
        return contratosService.listarContratosComEstado();
    }

    @GetMapping("/com-estado/cliente/{idcliente}")
    public List<ContratoClienteDTOBackend> listarContratosDoCliente(@PathVariable Long idcliente) {
        return contratosService.listarContratosPorCliente(idcliente);
    }


}

