package org.example;

import java.math.BigDecimal;
import java.time.LocalTime;

public final class Drink extends Product {


    Drink() {
    }

    Drink(int id, String name, BigDecimal price) {
        super(id, name, price);
    }

    Drink(int id, String name, BigDecimal price, Rating rating) {
        super(id, name, price, rating);
    }

    @Override
    public BigDecimal getDiscount() {
        LocalTime now = LocalTime.now();
        return (now.isAfter(LocalTime.of(10, 0)) && now.isBefore(LocalTime.of(18, 30)))
                ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Drink(getId(), getName(), getPrice(), newRating);
    }
}
