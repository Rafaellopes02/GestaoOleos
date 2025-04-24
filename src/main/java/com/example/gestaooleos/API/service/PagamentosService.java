package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.Pagamentos;
import com.example.gestaooleos.API.repository.PagamentosRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import java.util.Optional;

@Service
public class PagamentosService {
    private final PagamentosRepository pagamentosRepository;

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
                .filter(p -> p.getIdestadospagamento() == 2) // Supondo que 2 = Pago
                .filter(p -> YearMonth.from(p.getDatapagamento()).equals(mesAtual))
                .map(Pagamentos::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPendente = todos.stream()
                .filter(p -> p.getIdestadospagamento() == 1) // Supondo que 1 = Pendente
                .filter(p -> YearMonth.from(p.getDatapagamento()).equals(mesAtual))
                .map(Pagamentos::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> totais = new HashMap<>();
        totais.put("totalRecebido", totalRecebido);
        totais.put("totalPendente", totalPendente);
        return totais;
    }


}
