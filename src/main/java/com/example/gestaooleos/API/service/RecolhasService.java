package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.model.Recolhas;
import com.example.gestaooleos.API.repository.RecolhasRepository;
import org.springframework.stereotype.Service;
import com.example.gestaooleos.API.model.Pagamentos;
import com.example.gestaooleos.API.repository.PagamentosRepository;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
public class RecolhasService {

    @Autowired
    private PagamentosRepository pagamentosRepository;

    private final RecolhasRepository recolhasRepository;

    public RecolhasService(RecolhasRepository recolhasRepository) {
        this.recolhasRepository = recolhasRepository;
    }

    public Iterable<Recolhas> listarRecolhas() {
        return recolhasRepository.findAll();
    }

    public Optional<Recolhas> obterRecolhas(Long id) {
        return recolhasRepository.findById(id);
    }

    public Recolhas criarRecolhas(Recolhas recolha) {
        Recolhas recolhaCriada = recolhasRepository.save(recolha);

        // Calcula o valor com base na quantidade
        double quantidade = recolha.getQuantidade();
        int numBidoes = (int) Math.ceil(quantidade / 5000.0);
        double valor = quantidade * 0.002;

        // Cria o pagamento associado
        Pagamentos pagamento = new Pagamentos();
        pagamento.setIdcontrato(recolhaCriada.getIdcontrato());
        pagamento.setValor(BigDecimal.valueOf(valor));
        pagamento.setIdmetodopagamento(4L);
        pagamento.setIdestadospagamento(1L);
        pagamento.setDatapagamento(null);

        pagamentosRepository.save(pagamento);

        return recolhaCriada;
    }


    public void removeRecolhas(Long id) {
        recolhasRepository.deleteById(id);
    }

    public void atualizarEstado(Long id, int novoEstadoId) {
        Optional<Recolhas> recolhaOpt = recolhasRepository.findById(id);
        if (recolhaOpt.isPresent()) {
            Recolhas recolha = recolhaOpt.get();
            recolha.setIdestadorecolha(novoEstadoId);
            recolhasRepository.save(recolha);
        }
    }

    public List<Recolhas> listarPorEstado(int estadoId) {
        return recolhasRepository.findByIdestadorecolha(estadoId);
    }


    public int contarPorEstado(int estadoId) {
        return (int) recolhasRepository.countByIdestadorecolha(estadoId);
    }

    public void notificarEmpregado(Long idRecolha, int novoEstadoId, Long idEmpregado) {
        Optional<Recolhas> recolhaOpt = recolhasRepository.findById(idRecolha);
        if (recolhaOpt.isPresent()) {
            Recolhas recolha = recolhaOpt.get();

            System.out.println("🔁 Atualizando recolha " + idRecolha +
                    " para estado " + novoEstadoId +
                    " com empregado " + idEmpregado);

            recolha.setIdestadorecolha(novoEstadoId);
            recolha.setIdutilizador(idEmpregado); // <- VERIFICA ESTE CAMPO

            recolhasRepository.save(recolha);
        } else {
            throw new RuntimeException("❌ Recolha não encontrada com ID: " + idRecolha);
        }
    }

    public void atualizarObservacoes(Long idRecolha, String novasObservacoes) {
        Optional<Recolhas> optional = recolhasRepository.findById(idRecolha);
        if (optional.isPresent()) {
            Recolhas recolha = optional.get();
            recolha.setObservacoes(novasObservacoes);
            recolhasRepository.save(recolha);
        } else {
            throw new RuntimeException("Recolha com ID " + idRecolha + " não encontrada.");
        }
    }





}
