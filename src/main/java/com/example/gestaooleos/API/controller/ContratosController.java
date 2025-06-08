package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.dto.*;
import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.service.ContratosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            ex.printStackTrace();
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
    @GetMapping("/contar-estados/utilizador/{id}")
    public Map<String, Long> contarContratosPorEstadoDoUtilizador(@PathVariable Long id) {
        return contratosService.contarContratosPorEstadoDoUtilizador(id);
    }

    @PatchMapping("/Contratos/{id}/estado")
    public ResponseEntity<?> atualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String novoEstado = payload.get("novoEstado");
        contratosService.atualizarEstadoContrato(id, novoEstado);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarContrato(@PathVariable Long id, @RequestBody ContratoDTOBackend dto) {
        Optional<Contratos> contratoExistente = contratosService.obterContratos(id);
        if (contratoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            java.sql.Date dataInicio = java.sql.Date.valueOf(LocalDate.parse(dto.getDataInicio(), formatter));
            java.sql.Date dataFim = java.sql.Date.valueOf(LocalDate.parse(dto.getDataFim(), formatter));

            Contratos contrato = new Contratos();
            contrato.setIdcontrato(id);
            contrato.setNome(dto.getNome());
            contrato.setDatainicio(dataInicio);
            contrato.setDatafim(dataFim);
            contrato.setIdutilizador(dto.getIdutilizador());
            contrato.setIdestadocontrato(dto.getIdEstadoContrato());

            contratosService.atualizarContrato(contrato);
            return ResponseEntity.noContent().build();

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Erro ao atualizar contrato: " + ex.getMessage());
        }
    }


}

