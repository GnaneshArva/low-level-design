
package com.food.delivery;

public class DeliveryAgent {
    private final String id;
    private boolean available = true;

    public DeliveryAgent(String id) {
        this.id = id;
    }

    public boolean isAvailable() { return available; }
    public void assign() { available = false; }
    public String getId() { return id; }
}
