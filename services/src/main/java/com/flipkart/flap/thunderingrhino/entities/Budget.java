package com.flipkart.flap.thunderingrhino.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Created by pavan.t on 01/06/15.
 */
public class Budget {
    private String currency;
    private BigDecimal value;

    public Budget() {}

    public Budget(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    @JsonProperty
    public String getCurrency() {
        return currency;
    }

    @JsonProperty
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty
    public BigDecimal getValue() {
        return value;
    }

    @JsonProperty
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Budget budget = (Budget) o;

        if (currency != null ? !currency.equals(budget.currency) : budget.currency != null) return false;
        if (value != null ? !value.equals(budget.value) : budget.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currency != null ? currency.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}