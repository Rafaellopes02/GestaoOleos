package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.dto.PedidoContratoDetalhadoDTO;
import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.model.PedidosContrato;
import com.example.gestaooleos.API.model.Utilizadores;
import com.example.gestaooleos.API.repository.ContratosRepository;
import com.example.gestaooleos.API.repository.PedidosContratoRepository;
import com.example.gestaooleos.API.repository.UtilizadoresRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidosContratoService {
    private final PedidosContratoRepository pedidosContratoRepository;
    private final ContratosRepository contratoRepository;
    private final UtilizadoresRepository utilizadorRepository;

    public PedidosContratoService(PedidosContratoRepository pedidosContratoRepository,
                                  ContratosRepository contratoRepository,
                                  UtilizadoresRepository utilizadorRepository) {
        this.pedidosContratoRepository = pedidosContratoRepository;
        this.contratoRepository = contratoRepository;
        this.utilizadorRepository = utilizadorRepository;
    }

    public Iterable<PedidosContrato> listarPedidosContrato() {
        return pedidosContratoRepository.findAll();
    }

    public Optional<PedidosContrato> obterPedidosContrato(Long id) {
        return pedidosContratoRepository.findById(id);
    }

    public PedidosContrato criarPedidosContrato(PedidosContrato pedidocontrato) {
        return pedidosContratoRepository.save(pedidocontrato);
    }

    public void removePedidosContrato(Long id) {
        pedidosContratoRepository.deleteById(id);
    }

    public long contarPedidosPendentes() {
        return pedidosContratoRepository.countByIdestadopedido(1);
    }

    public List<PedidoContratoDetalhadoDTO> listarPedidosComDetalhes() {
        List<PedidosContrato> pedidos = (List<PedidosContrato>) pedidosContratoRepository.findAll();
        List<PedidoContratoDetalhadoDTO> detalhes = new ArrayList<>();

        for (PedidosContrato pedido : pedidos) {
            Optional<Contratos> contratoOpt = contratoRepository.findById((long) pedido.getIdcontrato());

            if (contratoOpt.isPresent()) {
                Contratos contrato = contratoOpt.get();
                Optional<Utilizadores> utilizadorOpt = utilizadorRepository.findById((long) contrato.getIdutilizador());

                String nomeUtilizador = utilizadorOpt.map(Utilizadores::getNome).orElse("Desconhecido");

                detalhes.add(new PedidoContratoDetalhadoDTO(
                        pedido.getIdpedidocontrato(),
                        contrato.getNome(),
                        contrato.getDatainicio().toString(),
                        contrato.getDatafim().toString(),
                        nomeUtilizador,
                        pedido.getIdestadopedido()
                ));
            }
        }

        return detalhes;
    }

    public void atualizarEstadoPedido(Long idPedido, int novoEstadoPedido, int novoEstadoContrato) {
        Optional<PedidosContrato> pedidoOpt = pedidosContratoRepository.findById(idPedido);
        if (pedidoOpt.isPresent()) {
            PedidosContrato pedido = pedidoOpt.get();
            pedido.setIdestadopedido(novoEstadoPedido);
            pedidosContratoRepository.save(pedido);

            // Atualizar estado do contrato
            Long idContrato = (long) pedido.getIdcontrato(); // cast para Long
            contratoRepository.findById(idContrato).ifPresent(contrato -> {
                contrato.setIdestadocontrato(novoEstadoContrato);
                contratoRepository.save(contrato);
            });
        }
    }

}
