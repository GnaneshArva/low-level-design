
package com.food.order.state;

import com.food.order.Order;

public class OutForDeliveryState implements OrderState {

    public void next(Order order) {
        order.setState(new DeliveredState());
    }

    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel in delivery");
    }

    public String name() { return "OUT_FOR_DELIVERY"; }
}
