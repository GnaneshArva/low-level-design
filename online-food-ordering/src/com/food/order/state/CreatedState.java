
package com.food.order.state;

import com.food.order.Order;

public class CreatedState implements OrderState {

    public void next(Order order) {
        order.setState(new PaidState());
    }

    public void cancel(Order order) {
        order.setState(new CancelledState());
    }

    public String name() { return "CREATED"; }
}
