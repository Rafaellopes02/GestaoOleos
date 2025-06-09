package com.example.gestaooleos.API.mapper;

import com.example.gestaooleos.API.dto.RecolhaDTOBackend;
import com.example.gestaooleos.API.model.Recolhas;

public class RecolhaMapper {

    public static RecolhaDTOBackend toDTO(Recolhas r) {
        RecolhaDTOBackend dto = new RecolhaDTOBackend();

        dto.setIdrecolha(r.getIdrecolha());
        dto.setData(r.getData().toString()); // ou usa formatador se quiseres
        dto.setQuantidade(r.getQuantidade());
        dto.setObservacoes(r.getObservacoes());
        dto.setNumbidoes(r.getNumbidoes());
        dto.setMorada(r.getMorada());


        // 🚨 Linha ESSENCIAL que estava faltando:
        dto.setIdestadorecolha(r.getIdestadorecolha());

        return dto;
    }
}
