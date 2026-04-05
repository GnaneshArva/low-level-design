
package com.food.order.state;

import com.food.order.Order;

public class PaidState implements OrderState {

    public void next(Order order) {
        order.setState(new PreparingState());
    }

    public void cancel(Order order) {
        order.setState(new CancelledState());
    }

    public String name() { return "PAID"; }
}
