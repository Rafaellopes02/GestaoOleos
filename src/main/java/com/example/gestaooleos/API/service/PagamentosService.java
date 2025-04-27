package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.dto.TotalRecebidoPorDiaDTO;
import com.example.gestaooleos.API.model.Pagamentos;
import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.dto.PagamentoDTOBackend;
import com.example.gestaooleos.API.repository.PagamentosRepository;
import com.example.gestaooleos.API.repository.ContratosRepository;
import com.example.gestaooleos.API.repository.UtilizadoresRepository;
import com.example.gestaooleos.API.repository.EstadosPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PagamentosService {

    private final PagamentosRepository pagamentosRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ContratosRepository contratosRepository;
    private final UtilizadoresRepository utilizadoresRepository;
    private final EstadosPagamentoRepository estadosPagamentoRepository;

    @Autowired
    public PagamentosService(PagamentosRepository pagamentosRepository,
                             JdbcTemplate jdbcTemplate,
                             ContratosRepository contratosRepository,
                             UtilizadoresRepository utilizadoresRepository,
                             EstadosPagamentoRepository estadosPagamentoRepository) {
        this.pagamentosRepository = pagamentosRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.contratosRepository = contratosRepository;
        this.utilizadoresRepository = utilizadoresRepository;
        this.estadosPagamentoRepository = estadosPagamentoRepository;
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
                .filter(p -> p.getDatapagamento() != null && YearMonth.from(p.getDatapagamento()).equals(mesAtual))
                .map(Pagamentos::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPendente = todos.stream()
                .filter(p -> p.getIdestadospagamento() == 2)
                .filter(p -> p.getDatapagamento() != null && YearMonth.from(p.getDatapagamento()).equals(mesAtual))
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
            dto.setDatapagamento(
                    pagamento.getDatapagamento() != null ? pagamento.getDatapagamento().toString() : "Ainda não foi pago"
            );
            dto.setValor(pagamento.getValor());

            contratosRepository.findById(pagamento.getIdcontrato())
                    .ifPresent(contrato -> dto.setNomeContrato(contrato.getNome()));

            contratosRepository.findById(pagamento.getIdcontrato())
                    .flatMap(contrato -> utilizadoresRepository.findById(Long.valueOf(contrato.getIdutilizador())))
                    .ifPresent(user -> dto.setNomeCliente(user.getNome()));

            estadosPagamentoRepository.findById(pagamento.getIdestadospagamento())
                    .ifPresent(estado -> dto.setEstado(estado.getNome()));

            return dto;
        }).collect(Collectors.toList());
    }

    public List<TotalRecebidoPorDiaDTO> listarTotaisRecebidosPorDia() {
        String sql = "SELECT datapagamento, SUM(valor) AS total FROM pagamentos WHERE idestadospagamento = 5 GROUP BY datapagamento ORDER BY datapagamento ASC";

        RowMapper<TotalRecebidoPorDiaDTO> rowMapper = (rs, rowNum) -> {
            String data = rs.getDate("datapagamento").toLocalDate().toString();
            BigDecimal total = rs.getBigDecimal("total");
            return new TotalRecebidoPorDiaDTO(data, total);
        };

        return jdbcTemplate.query(sql, rowMapper);
    }

    // --------------- NOVOS MÉTODOS AQUI ---------------- //

    public List<Pagamentos> listarPagamentosPendentesCliente(Long idutilizador) {
        List<Contratos> contratos = contratosRepository.findByIdutilizador(idutilizador);

        if (contratos.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> idsContratos = contratos.stream()
                .map(Contratos::getIdcontrato)
                .collect(Collectors.toList());

        List<Pagamentos> todosPagamentos = StreamSupport
                .stream(pagamentosRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return todosPagamentos.stream()
                .filter(p -> idsContratos.contains(p.getIdcontrato()) && p.getIdestadospagamento() == 2)
                .collect(Collectors.toList());
    }


    @Transactional
    public void pagarPagamento(Long idPagamento, Long idMetodoPagamento) {
        Pagamentos pagamento = pagamentosRepository.findById(idPagamento)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setIdestadospagamento(5L); // 1 = Pago
        pagamento.setIdmetodopagamento(idMetodoPagamento);
        pagamento.setDatapagamento(LocalDate.now());

        contratosRepository.findById(pagamento.getIdcontrato())
                .ifPresent(contrato -> {
                    contrato.setIdestadocontrato(1); // 1 = Ativo
                    contratosRepository.save(contrato);
                });
        pagamentosRepository.save(pagamento);
    }


    @Transactional
    public void cancelarPagamentoEContrato(Long idPagamento) {
        Optional<Pagamentos> pagamentoOpt = pagamentosRepository.findById(idPagamento);
        if (pagamentoOpt.isPresent()) {
            Pagamentos pagamento = pagamentoOpt.get();
            pagamento.setIdestadospagamento(8L); // 3 = Cancelado
            pagamentosRepository.save(pagamento);

            contratosRepository.findById(pagamento.getIdcontrato())
                    .ifPresent(contrato -> {
                        contrato.setIdestadocontrato(3); // 2 = Suspenso
                        contratosRepository.save(contrato);
                    });
        } else {
            throw new RuntimeException("Pagamento não encontrado.");
        }
    }
}
