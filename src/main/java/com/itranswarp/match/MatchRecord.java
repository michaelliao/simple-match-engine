package com.itranswarp.match;

import java.math.BigDecimal;

public class MatchRecord {

    public final BigDecimal price;
    public final BigDecimal amount;
    public final Order takerOrder;
    public final Order makerOrder;

    public MatchRecord(BigDecimal price, BigDecimal amount, Order takerOrder, Order makerOrder) {
        this.price = price;
        this.amount = amount;
        this.takerOrder = takerOrder;
        this.makerOrder = makerOrder;
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f]", this.price, this.amount);
    }
}
