package com.example.gestaooleos.UI.api;

import java.math.BigDecimal;

public class TotalRecebidoPorDiaDTO {
    private String data;
    private BigDecimal total;

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
