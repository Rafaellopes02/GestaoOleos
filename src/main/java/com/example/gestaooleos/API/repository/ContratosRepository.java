package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.Contratos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratosRepository extends CrudRepository<Contratos, Long> {
}
