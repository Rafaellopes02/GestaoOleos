package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.dto.*;
import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.model.Pagamentos;
import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.model.EstadosContratos;
import com.example.gestaooleos.API.repository.*;
import com.example.gestaooleos.API.model.PedidosContrato;
import com.example.gestaooleos.API.repository.PedidosContratoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.*;
import java.util.stream.StreamSupport;



@Service
public class ContratosService {

    private final ContratosRepository contratosRepository;
    private final EstadosContratosRepository estadosContratosRepository;


    @Autowired
    private PedidosContratoRepository pedidosContratoRepository;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private UtilizadoresRepository utilizadoresRepository;

    @Autowired
    private EstadosPagamentoRepository estadosPagamentoRepository;

    public ContratosService(ContratosRepository contratosRepository, EstadosContratosRepository estadosContratosRepository) {
        this.contratosRepository = contratosRepository;
        this.estadosContratosRepository = estadosContratosRepository;
    }

    public Iterable<Contratos> listarContratos() {
        return contratosRepository.findAll();
    }

    public Optional<Contratos> obterContratos(Long id) {
        return contratosRepository.findById(id);
    }

    public Contratos criarContratoComPagamento(ContratoDTOBackend contratoDTO) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataInicioLocalDate = LocalDate.parse(contratoDTO.getDataInicio(), formatter);
            LocalDate dataFimLocalDate = LocalDate.parse(contratoDTO.getDataFim(), formatter);
            Date datainicio = Date.valueOf(dataInicioLocalDate);
            Date datafim = Date.valueOf(dataFimLocalDate);

            Contratos contrato = new Contratos();
            contrato.setNome(contratoDTO.getNome());
            contrato.setDatainicio(datainicio);
            contrato.setDatafim(datafim);
            contrato.setIdutilizador(contratoDTO.getIdutilizador());
            contrato.setIdestadocontrato(contratoDTO.getIdEstadoContrato());

            Contratos contratoCriado = contratosRepository.save(contrato);

            PedidosContrato pedido = new PedidosContrato();
            pedido.setData(new java.sql.Date(System.currentTimeMillis()));
            pedido.setIdcontrato(contratoCriado.getIdcontrato().intValue());
            pedido.setIdestadopedido(1);
            pedidosContratoRepository.save(pedido);

            return contratoCriado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar contrato: " + e.getMessage(), e);
        }
    }

    public void removeContratos(Long id) {
        contratosRepository.deleteById(id);
    }

    public Map<String, Long> contarContratosPorEstado() {
        Iterable<Contratos> contratos = contratosRepository.findAll();

        long ativos = StreamSupport.stream(contratos.spliterator(), false)
                .filter(c -> c.getIdestadocontrato() == 1)
                .count();

        long suspensos = StreamSupport.stream(contratos.spliterator(), false)
                .filter(c -> c.getIdestadocontrato() == 2)
                .count();

        long concluidos = StreamSupport.stream(contratos.spliterator(), false)
                .filter(c -> c.getIdestadocontrato() == 3)
                .count();

        Map<String, Long> contadores = new HashMap<>();
        contadores.put("ativos", ativos);
        contadores.put("suspensos", suspensos);
        contadores.put("concluidos", concluidos);
        return contadores;
    }

    public List<ContratoDTOBackend> listarContratosComEstado() {
        Iterable<Contratos> contratos = contratosRepository.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return StreamSupport.stream(contratos.spliterator(), false)
                .map(c -> {
                    ContratoDTOBackend dto = new ContratoDTOBackend();
                    dto.setIdcontrato(c.getIdcontrato());
                    dto.setNome(c.getNome());
                    dto.setDataInicio(c.getDatainicio() != null ? sdf.format(c.getDatainicio()) : "");
                    dto.setDataFim(c.getDatafim() != null ? sdf.format(c.getDatafim()) : "");
                    dto.setIdEstadoContrato(c.getIdestadocontrato());
                    dto.setIdutilizador(c.getIdutilizador());
                    String nomeEstado = estadosContratosRepository.findById((long) c.getIdestadocontrato())
                            .map(EstadosContratos::getNome)
                            .orElse("Desconhecido");
                    dto.setEstado(nomeEstado);
                    return dto;
                })
                .toList();
    }

    public List<ContratoClienteDTOBackend> listarContratosPorCliente(Long idcliente) {
        List<Contratos> contratos = contratosRepository.findByIdutilizador(idcliente);

        return contratos.stream().map(contrato -> {
            ContratoClienteDTOBackend dto = new ContratoClienteDTOBackend();
            dto.setIdcontrato(contrato.getIdcontrato());
            dto.setNomeContrato(contrato.getNome());
            dto.setDataInicio(contrato.getDatainicio().toString());
            dto.setDataFim(contrato.getDatafim().toString());

            String estado;
            if (contrato.getIdestadocontrato() == 1) {
                estado = "Ativo";
            } else {
                estado = "Inativo";
            }
            dto.setEstado(estado);

            Utilizadores utilizador = utilizadoresRepository.findById((long) contrato.getIdutilizador()).orElse(null);
            if (utilizador != null) {
                dto.setMoradaCliente(utilizador.getMorada());
            } else {
                dto.setMoradaCliente("Desconhecida");
            }

            return dto;
        }).toList();
    }

    public Map<String, Long> contarContratosPorEstadoDoUtilizador(Long idUtilizador) {
        List<Contratos> contratos = contratosRepository.findByIdutilizador(idUtilizador);

        long ativos = contratos.stream()
                .filter(c -> c.getIdestadocontrato() == 1)
                .count();

        long suspensos = contratos.stream()
                .filter(c -> c.getIdestadocontrato() == 2)
                .count();

        long concluidos = contratos.stream()
                .filter(c -> c.getIdestadocontrato() == 3)
                .count();

        Map<String, Long> contadores = new HashMap<>();
        contadores.put("ativos", ativos);
        contadores.put("suspensos", suspensos);
        contadores.put("concluidos", concluidos);
        return contadores;
    }


}
