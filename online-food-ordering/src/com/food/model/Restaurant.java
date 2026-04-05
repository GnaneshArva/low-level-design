
package com.food.model;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private final String restaurantId;
    private final String name;
    private final List<MenuItem> menu = new ArrayList<>();

    public Restaurant(String restaurantId, String name) {
        this.restaurantId = restaurantId;
        this.name = name;
    }

    public void addMenuItem(MenuItem item) {
        menu.add(item);
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public String getRestaurantId() { return restaurantId; }
    public String getName() { return name; }
}
