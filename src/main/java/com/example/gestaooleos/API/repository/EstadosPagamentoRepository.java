package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.EstadosPagamento;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EstadosPagamentoRepository extends CrudRepository<EstadosPagamento, Long> {
    Optional<EstadosPagamento> findByNome(String nome);
}
