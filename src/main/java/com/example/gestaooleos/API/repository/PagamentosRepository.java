package com.example.gestaooleos.API.repository;

import com.example.gestaooleos.API.model.Pagamentos;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PagamentosRepository extends CrudRepository<Pagamentos, Long> {

    @Query("SELECT p.datapagamento, SUM(p.valor) " +
            "FROM pagamentos p " +
            "WHERE p.idestadospagamento = 5 " +
            "AND p.datapagamento >= CURRENT_DATE - INTERVAL '7 days' " +
            "GROUP BY p.datapagamento " +
            "ORDER BY p.datapagamento ASC")
    List<Object[]> somarPorDiaUltimos7();



}
