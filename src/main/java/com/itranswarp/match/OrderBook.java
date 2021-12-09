package com.itranswarp.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import java.util.TreeMap;

public class OrderBook {

    private static final Comparator<OrderKey> SORT_SELL = new Comparator<>() {
        @Override
        public int compare(OrderKey o1, OrderKey o2) {
            // 价格低在前:
            int cmp = o1.price.compareTo(o2.price);
            // 时间早在前:
            return cmp == 0 ? Long.compare(o1.sequenceId, o2.sequenceId) : cmp;
        }
    };

    private static final Comparator<OrderKey> SORT_BUY = new Comparator<>() {
        @Override
        public int compare(OrderKey o1, OrderKey o2) {
            // 价格高在前:
            int cmp = o2.price.compareTo(o1.price);
            // 时间早在前:
            return cmp == 0 ? Long.compare(o1.sequenceId, o2.sequenceId) : cmp;
        }
    };

    public final Direction direction;
    public final TreeMap<OrderKey, Order> book;

    public OrderBook(Direction direction) {
        this.direction = direction;
        this.book = new TreeMap<>(direction == Direction.BUY ? SORT_BUY : SORT_SELL);
    }

    public Order getFirst() {
        return this.book.isEmpty() ? null : this.book.firstEntry().getValue();
    }

    public boolean remove(Order order) {
        return this.book.remove(new OrderKey(order.sequenceId, order.price)) != null;
    }

    public boolean add(Order order) {
        return this.book.put(new OrderKey(order.sequenceId, order.price), order) == null;
    }

    public int size() {
        return this.book.size();
    }

    @Override
    public String toString() {
        if (this.book.isEmpty()) {
            return "(empty)";
        }
        List<String> orders = new ArrayList<>(10);
        for (Entry<OrderKey, Order> entry : this.book.entrySet()) {
            orders.add(entry.getValue().toString());
        }
        if (direction == Direction.SELL) {
            Collections.reverse(orders);
        }
        return String.join("\n", orders);
    }
}
