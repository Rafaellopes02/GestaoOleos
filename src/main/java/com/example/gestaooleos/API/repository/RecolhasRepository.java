package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.Recolhas;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecolhasRepository extends CrudRepository<Recolhas, Long> {
    long countByIdestadorecolha(int idestadorecolha);
    List<Recolhas> findByIdestadorecolha(int idestadorecolha);
    List<Recolhas> findByIdutilizadorAndIdestadorecolha(Long idutilizador, int idestadorecolha);




}