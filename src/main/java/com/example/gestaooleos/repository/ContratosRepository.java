package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.Contratos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratosRepository extends CrudRepository<Contratos, Long> {
}
