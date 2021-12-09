package com.itranswarp.match;

import java.math.BigDecimal;

public class Order {

    public final long sequenceId;
    public final Direction direction;
    public final BigDecimal price;
    public BigDecimal amount;

    public Order(long sequenceId, Direction direction, BigDecimal price, BigDecimal amount) {
        this.sequenceId = sequenceId;
        this.direction = direction;
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %s seq=%s", this.price, this.amount, this.direction, this.sequenceId);
    }
}
