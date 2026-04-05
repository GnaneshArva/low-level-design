
package com.food.order;

import com.food.cart.CartItem;
import com.food.order.state.CreatedState;
import com.food.order.state.OrderState;

import java.util.List;

public class Order {

    private final String orderId;
    private final List<CartItem> items;
    private final double total;
    private OrderState state;

    public Order(String orderId, List<CartItem> items, double total) {
        this.orderId = orderId;
        this.items = items;
        this.total = total;
        this.state = new CreatedState();
    }

    public void nextState() {
        state.next(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public String getStatus() {
        return state.name();
    }

    public double getTotal() { return total; }
    public String getOrderId() { return orderId; }
}
