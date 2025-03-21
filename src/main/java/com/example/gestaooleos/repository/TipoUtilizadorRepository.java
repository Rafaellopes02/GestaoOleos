package com.example.gestaooleos.repository;

import com.example.gestaooleos.model.TipoUtilizador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUtilizadorRepository extends CrudRepository<TipoUtilizador, Long> {
}
