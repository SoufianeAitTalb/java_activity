package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public non-sealed class Food extends Product {

    private LocalDate bestBefore;


    Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    @Override
    public LocalDate getBestBefore() {
        return bestBefore;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Food food = (Food) o;
        return Objects.equals(bestBefore, food.bestBefore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bestBefore);
    }


    @Override
    public BigDecimal getDiscount() {
        return (bestBefore.isEqual(LocalDate.now())) ? super.getDiscount() : BigDecimal.ZERO;
    }

    @Override
    public Product applyRating(Rating newRating) {
        return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
    }
}
