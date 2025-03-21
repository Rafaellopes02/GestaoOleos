package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.EstadosPedidos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosPedidosRepository extends CrudRepository<EstadosPedidos, Long> {
}
