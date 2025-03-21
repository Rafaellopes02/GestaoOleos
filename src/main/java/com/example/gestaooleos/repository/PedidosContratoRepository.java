package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.PedidosContrato;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidosContratoRepository extends CrudRepository<PedidosContrato, Long> {
}
