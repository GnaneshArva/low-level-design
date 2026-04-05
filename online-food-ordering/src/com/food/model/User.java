
package com.food.model;

public class User {
    private final String userId;
    private final String name;
    private final String address;

    public User(String userId, String name, String address) {
        this.userId = userId;
        this.name = name;
        this.address = address;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
}
