package com.itranswarp.match;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class MatchEngineTest {

    long sequenceId = 0;

    @Test
    public void testMatchEngine() {
        String inputs = """
                buy  2082.34 1
                sell 2087.6  2
                buy  2087.8  1
                buy  2085.01 5
                sell 2088.02 3
                sell 2087.60 6
                buy  2081.11 7
                buy  2086.0  3
                buy  2088.33 1
                sell 2086.54 2
                sell 2086.55 5
                buy  2086.55 3
                """;
        MatchEngine engine = new MatchEngine();
        for (String input : inputs.strip().lines().toArray(String[]::new)) {
            Order order = createOrder(input);
            System.out.println("\n================================================================================\n");
            System.out.println("process order: " + order);
            MatchResult result = engine.processOrder(order);
            System.out.println(engine);
            System.out.println(result);
        }
    }

    Order createOrder(String input) {
        sequenceId++;
        String[] ss = input.split("\\s+");
        Direction direction = Direction.valueOf(ss[0].toUpperCase());
        BigDecimal price = new BigDecimal(ss[1]);
        BigDecimal amount = new BigDecimal(ss[2]);
        return new Order(sequenceId, direction, price, amount);
    }
}
