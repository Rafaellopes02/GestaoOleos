package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.PedidosContrato;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosContratoRepository extends CrudRepository<PedidosContrato, Long> {
}
