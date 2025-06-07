package com.example.gestaooleos.API.controller;

import com.example.gestaooleos.API.dto.PagamentoDTOBackend;
import com.example.gestaooleos.API.dto.TotalRecebidoPorDiaDTO;
import com.example.gestaooleos.API.model.Pagamentos;
import com.example.gestaooleos.API.service.PagamentosService;
import com.example.gestaooleos.API.dto.MetodoPagamentoRequest;

import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register")
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

    @GetMapping("/completos")
    public List<PagamentoDTOBackend> listarPagamentosCompletos() {
        return pagamentosService.listarPagamentosCompletos();
    }

    @GetMapping("/totais-dia")
    public List<TotalRecebidoPorDiaDTO> listarTotaisRecebidosPorDia() {
        return pagamentosService.listarTotaisRecebidosPorDia();
    }

    // Listar pendentes do cliente
    @GetMapping("/pendentes/{idcliente}")
    public List<Pagamentos> listarPendentesCliente(@PathVariable Long idcliente) {
        return pagamentosService.listarPagamentosPendentesCliente(idcliente);
    }

    // Marcar pagamento como pago
    @PutMapping("/pagar/{idpagamento}")
    public ResponseEntity<Void> pagarPagamento(@PathVariable Long idpagamento, @RequestParam Long idmetodopagamento) {
        pagamentosService.pagarPagamento(idpagamento, idmetodopagamento);
        return ResponseEntity.ok().build();
    }

    // Cancelar pagamento e suspender contrato
    @PutMapping("/cancelar/{idpagamento}")
    public void cancelarPagamento(@PathVariable Long idpagamento) {
        pagamentosService.cancelarPagamentoEContrato(idpagamento);
    }

    @PostMapping("/{id}/concluir")
    public ResponseEntity<Void> concluirPagamento(@PathVariable Long id, @RequestBody MetodoPagamentoRequest request) {
        pagamentosService.pagarPagamento(id, request.getIdmetodopagamento());
        return ResponseEntity.noContent().build();
    }
}
