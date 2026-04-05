
package com.food.order.state;

import com.food.order.Order;

public class PreparingState implements OrderState {

    public void next(Order order) {
        order.setState(new OutForDeliveryState());
    }

    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel after preparation");
    }

    public String name() { return "PREPARING"; }
}
