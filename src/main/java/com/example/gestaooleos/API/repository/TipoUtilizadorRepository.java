package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.TipoUtilizador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUtilizadorRepository extends CrudRepository<TipoUtilizador, Long> {
}
