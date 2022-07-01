package com.itranswarp.match;

import java.math.BigDecimal;

public class MatchEngine {

    public final OrderBook buyBook = new OrderBook(Direction.BUY);
    public final OrderBook sellBook = new OrderBook(Direction.SELL);
    public BigDecimal marketPrice = BigDecimal.ZERO; // 最新市场价

    public MatchResult processOrder(Order order) {
        switch (order.direction) {
        case BUY:
            return processOrder(order, this.sellBook, this.buyBook);
        case SELL:
            return processOrder(order, this.buyBook, this.sellBook);
        default:
            throw new IllegalArgumentException("Invalid direction.");
        }
    }

    /**
     * @param takerOrder  输入订单
     * @param makerBook   尝试匹配成交的OrderBook
     * @param anotherBook 未能完全成交后挂单的OrderBook
     * @return 成交结果
     */
    MatchResult processOrder(Order takerOrder, OrderBook makerBook, OrderBook anotherBook) {
        MatchResult matchResult = new MatchResult(takerOrder);
        for (;;) {
            Order makerOrder = makerBook.getFirst();
            if (makerOrder == null) {
                // 对手盘不存在:
                break;
            }
            if (takerOrder.direction == Direction.BUY && takerOrder.price.compareTo(makerOrder.price) < 0) {
                // 买入订单价格比卖盘第一档价格低:
                break;
            } else if (takerOrder.direction == Direction.SELL && takerOrder.price.compareTo(makerOrder.price) > 0) {
                // 卖出订单价格比买盘第一档价格高:
                break;
            }
            // 以Maker价格成交:
            this.marketPrice = makerOrder.price;
            // 成交数量为两者较小值:
            BigDecimal matchedAmount = takerOrder.amount.min(makerOrder.amount);
            // 成交记录:
            matchResult.add(makerOrder.price, matchedAmount, makerOrder);
            // 更新成交后的订单数量:
            takerOrder.amount = takerOrder.amount.subtract(matchedAmount);
            makerOrder.amount = makerOrder.amount.subtract(matchedAmount);
            // 对手盘完全成交后，从订单簿中删除:
            if (makerOrder.amount.signum() == 0) {
                makerBook.remove(makerOrder);
            }
            // Taker订单完全成交后，退出循环:
            if (takerOrder.amount.signum() == 0) {
                break;
            }
        }
        // Taker订单未完全成交时，放入订单簿:
        if (takerOrder.amount.signum() > 0) {
            anotherBook.add(takerOrder);
        }
        return matchResult;
    }

    public boolean cancel(Order order) {
        OrderBook book = order.direction == Direction.BUY ? this.buyBook : this.sellBook;
        return book.remove(order);
    }

    @Override
    public String toString() {
        String line = "\n-------------------------\n";
        return line + this.sellBook + line + this.marketPrice + line + this.buyBook + line;
    }
}
