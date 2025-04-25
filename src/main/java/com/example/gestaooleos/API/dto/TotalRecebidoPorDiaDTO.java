package com.example.gestaooleos.API.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TotalRecebidoPorDiaDTO {
    private String data;
    private BigDecimal total;

    public TotalRecebidoPorDiaDTO() {}

    public TotalRecebidoPorDiaDTO(String data, BigDecimal total) {
        this.data = data;
        this.total = total;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
