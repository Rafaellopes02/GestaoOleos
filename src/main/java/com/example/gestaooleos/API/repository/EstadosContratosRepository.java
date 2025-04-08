package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.EstadosContratos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosContratosRepository extends CrudRepository<EstadosContratos, Long> {
}
