package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.EstadosContratos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosContratosRepository extends CrudRepository<EstadosContratos, Long> {
}
