
package com.food.order.state;

import com.food.order.Order;

public class DeliveredState implements OrderState {

    public void next(Order order) {}

    public void cancel(Order order) {
        throw new IllegalStateException("Already delivered");
    }

    public String name() { return "DELIVERED"; }
}
