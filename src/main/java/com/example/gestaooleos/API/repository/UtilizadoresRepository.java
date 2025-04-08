package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.Utilizadores;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilizadoresRepository extends CrudRepository<Utilizadores, Long> {
}
