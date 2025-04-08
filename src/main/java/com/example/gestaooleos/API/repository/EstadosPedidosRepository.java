package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.EstadosPedidos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosPedidosRepository extends CrudRepository<EstadosPedidos, Long> {
}
