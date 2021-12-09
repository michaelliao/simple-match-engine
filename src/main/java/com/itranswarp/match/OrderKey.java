package com.itranswarp.match;

import java.math.BigDecimal;

public class OrderKey {

    public final long sequenceId;
    public final BigDecimal price;

    public OrderKey(long sequenceId, BigDecimal price) {
        this.sequenceId = sequenceId;
        this.price = price;
    }
}
