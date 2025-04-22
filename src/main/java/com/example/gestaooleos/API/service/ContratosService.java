package com.example.gestaooleos.API.service;

import com.example.gestaooleos.API.dto.ContratoDTOBackend;
import com.example.gestaooleos.API.model.Contratos;
import com.example.gestaooleos.API.model.EstadosContratos;
import com.example.gestaooleos.API.repository.ContratosRepository;
import com.example.gestaooleos.API.repository.EstadosContratosRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.StreamSupport;

@Service
public class ContratosService {
    private final ContratosRepository contratosRepository;
    private final EstadosContratosRepository estadosContratosRepository;

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

    public Contratos criarContratos(Contratos contrato) {
        return contratosRepository.save(contrato);
    }

    public void removeContratos(Long id) {
        contratosRepository.deleteById(id);
    }

    public Map<String, Long> contarContratosPorEstado() {
        Iterable<Contratos> contratos = contratosRepository.findAll();

        long ativos = StreamSupport.stream(contratos.spliterator(), false)
                .filter(c -> c.getIdestadocontrato() == 1)
                .count();

        long concluidos = StreamSupport.stream(contratos.spliterator(), false)
                .filter(c -> c.getIdestadocontrato() == 3)
                .count();

        Map<String, Long> contadores = new HashMap<>();
        contadores.put("ativos", ativos);
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

                    // Conversão de int para Long
                    Long idEstado = (long) c.getIdestadocontrato();

                    String nomeEstado = estadosContratosRepository.findById(idEstado)
                            .map(EstadosContratos::getNome)
                            .orElse("Desconhecido");

                    dto.setEstado(nomeEstado);
                    return dto;
                })
                .toList();
    }

}
