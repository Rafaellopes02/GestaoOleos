package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.MetodoPagamento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagamentoRepository extends CrudRepository<MetodoPagamento, Long> {
}
