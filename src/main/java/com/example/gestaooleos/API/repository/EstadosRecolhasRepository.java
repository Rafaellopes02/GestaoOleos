package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.EstadosRecolhas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadosRecolhasRepository extends CrudRepository<EstadosRecolhas, Long> {
}
