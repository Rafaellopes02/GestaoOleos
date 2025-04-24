package com.example.gestaooleos.API.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("pagamentos")
public class Pagamentos {
    @Id
    private Long idpagamento;

    private Long idcontrato;
    private LocalDate datapagamento;
    private BigDecimal valor;
    private Long idmetodopagamento;
    private Long idestadospagamento;
}
