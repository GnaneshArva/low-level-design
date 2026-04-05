
package com.food;

import com.food.cart.Cart;
import com.food.delivery.*;
import com.food.model.*;
import com.food.payment.*;
import com.food.service.OrderService;
import com.food.order.Order;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Restaurant r = new Restaurant("1", "Pizza Place");
        MenuItem pizza = new MenuItem("1", "Pizza", 10);
        MenuItem burger = new MenuItem("2", "Burger", 5);
        r.addMenuItem(pizza);
        r.addMenuItem(burger);

        Cart cart = new Cart();
        cart.addItem(pizza);
        cart.addItem(burger);

        OrderService service = new OrderService();
        Order order = service.placeOrder(cart);

        service.track(order);

        service.makePayment(order, new UpiPayment());
        service.track(order);

        DeliveryAgent agent = service.assignDelivery(order,
                new NearestAgentStrategy(),
                Arrays.asList(new DeliveryAgent("A1"),
                              new DeliveryAgent("A2")));

        System.out.println("Assigned agent: " + agent.getId());

        order.nextState();
        service.track(order);
    }
}
