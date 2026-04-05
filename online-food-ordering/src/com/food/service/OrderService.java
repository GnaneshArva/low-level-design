
package com.food.service;

import com.food.cart.Cart;
import com.food.cart.CartItem;
import com.food.order.Order;
import com.food.payment.PaymentStrategy;
import com.food.delivery.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderService {

    public Order placeOrder(Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart empty");
        }
        return new Order(UUID.randomUUID().toString(),
                new ArrayList<>(cart.getItems()),
                cart.getTotal());
    }

    public void makePayment(Order order, PaymentStrategy payment) {
        if (!payment.pay(order.getTotal())) {
            throw new RuntimeException("Payment failed");
        }
        order.nextState();
    }

    public DeliveryAgent assignDelivery(Order order,
                                        DeliveryStrategy strategy,
                                        List<DeliveryAgent> agents) {
        DeliveryAgent agent = strategy.assign(agents);
        order.nextState();
        return agent;
    }

    public void track(Order order) {
        System.out.println("Order status: " + order.getStatus());
    }
}
