package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.model.Pagamentos;
import com.example.gestaooleos.API.service.PagamentosService;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagamentos") // Caminho mais padronizado
public class PagamentosController {

    private final PagamentosService pagamentosService;

    public PagamentosController(PagamentosService pagamentosService) {
        this.pagamentosService = pagamentosService;
    }

    @GetMapping
    public Iterable<Pagamentos> listarPagamentos() {
        return pagamentosService.listarPagamentos();
    }

    @GetMapping("/{id}")
    public Optional<Pagamentos> obterPagamentos(@PathVariable Long id) {
        return pagamentosService.obterPagamentos(id);
    }

    @PostMapping
    public Pagamentos criarPagamentos(@RequestBody Pagamentos pagamento) {
        return pagamentosService.criarPagamentos(pagamento);
    }

    @DeleteMapping("/{id}")
    public void removePagamentos(@PathVariable Long id) {
        pagamentosService.removePagamentos(id);
    }

    @GetMapping("/totais")
    public Map<String, BigDecimal> obterTotais() {
        return pagamentosService.obterTotais();
    }


}
