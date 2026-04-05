package com.lld.shoppingcart.model;

import com.lld.shoppingcart.exception.InvalidProductException;
import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final String id;
    private final String name;
    private final BigDecimal price;

    public Product(String id, String name, BigDecimal price) {
        validateProduct(id, name, price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validateProduct(String id, String name, BigDecimal price) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidProductException("Product ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be null or empty.");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductException("Price must be greater than zero.");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
