
package com.food.order.state;

import com.food.order.Order;

public class CancelledState implements OrderState {

    public void next(Order order) {}

    public void cancel(Order order) {}

    public String name() { return "CANCELLED"; }
}
