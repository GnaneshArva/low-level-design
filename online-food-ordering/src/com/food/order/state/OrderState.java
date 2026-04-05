
package com.food.order.state;

import com.food.order.Order;

public interface OrderState {
    void next(Order order);
    void cancel(Order order);
    String name();
}
