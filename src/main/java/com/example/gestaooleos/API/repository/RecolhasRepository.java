package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.Recolhas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecolhasRepository extends CrudRepository<Recolhas, Long> {
    long countByIdestadorecolha(int idestadorecolha);

}