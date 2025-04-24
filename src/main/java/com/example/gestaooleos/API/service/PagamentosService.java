package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.Pagamentos;
import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.model.EstadosPagamento;
import com.example.gestaooleos.API.dto.PagamentoDTOBackend;
import com.example.gestaooleos.API.repository.PagamentosRepository;
import com.example.gestaooleos.API.repository.ContratosRepository;
import com.example.gestaooleos.API.repository.UtilizadoresRepository;
import com.example.gestaooleos.API.repository.EstadosPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PagamentosService {
    private final PagamentosRepository pagamentosRepository;

    @Autowired
    private ContratosRepository contratosRepository;

    @Autowired
    private UtilizadoresRepository utilizadoresRepository;

    @Autowired
    private EstadosPagamentoRepository estadosPagamentoRepository;

    public PagamentosService(PagamentosRepository pagamentoRepository) {
        this.pagamentosRepository = pagamentoRepository;
    }

    public Iterable<Pagamentos> listarPagamentos() {
        return pagamentosRepository.findAll();
    }

    public Optional<Pagamentos> obterPagamentos(Long id) {
        return pagamentosRepository.findById(id);
    }

    public Pagamentos criarPagamentos(Pagamentos pagamentos) {
        return pagamentosRepository.save(pagamentos);
    }

    public void removePagamentos(Long id) {
        pagamentosRepository.deleteById(id);
    }

    public Map<String, BigDecimal> obterTotais() {
        List<Pagamentos> todos = StreamSupport
                .stream(pagamentosRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        YearMonth mesAtual = YearMonth.now();

        BigDecimal totalRecebido = todos.stream()
                .filter(p -> p.getIdestadospagamento() == 5)
                .filter(p -> YearMonth.from(p.getDatapagamento()).equals(mesAtual))
                .map(Pagamentos::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPendente = todos.stream()
                .filter(p -> p.getIdestadospagamento() == 2)
                .filter(p -> YearMonth.from(p.getDatapagamento()).equals(mesAtual))
                .map(Pagamentos::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> totais = new HashMap<>();
        totais.put("totalRecebido", totalRecebido);
        totais.put("totalPendente", totalPendente);
        return totais;
    }

    public List<PagamentoDTOBackend> listarPagamentosCompletos() {
        List<Pagamentos> pagamentos = StreamSupport
                .stream(pagamentosRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return pagamentos.stream().map(pagamento -> {
            PagamentoDTOBackend dto = new PagamentoDTOBackend();
            dto.setIdpagamento(pagamento.getIdpagamento());
            dto.setDatapagamento(pagamento.getDatapagamento().toString());
            dto.setValor(pagamento.getValor());

            // Nome do contrato
            contratosRepository.findById(pagamento.getIdcontrato())
                    .ifPresent(contrato -> dto.setNomeContrato(contrato.getNome()));

            // Nome do cliente
            contratosRepository.findById(pagamento.getIdcontrato())
                    .flatMap(contrato -> utilizadoresRepository.findById(Long.valueOf(contrato.getIdutilizador())))
                    .ifPresent(user -> dto.setNomeCliente(user.getNome()));

            // Estado do pagamento
            estadosPagamentoRepository.findById(pagamento.getIdestadospagamento())
                    .ifPresent(estado -> dto.setEstado(estado.getNome()));

            return dto;
        }).collect(Collectors.toList());
    }
}
