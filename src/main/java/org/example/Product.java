package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public sealed abstract class Product permits Drink, Food {

    public static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.1");

    private int id;
    private String name;
    private BigDecimal price;
    private Rating rating;

    Product() {
    }

    Product(int id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Rating getRating() {
        return rating;
    }


    public BigDecimal getDiscount() {
        return price.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }


    public abstract Product applyRating(Rating newRating);

    public LocalDate getBestBefore() {
        return LocalDate.now();
    }

    @Override
    public String toString() {
        return
                id + ", " +
                        name + ", " +
                        price + ", " +
                        getDiscount() + ", " +
                        rating + ", " +
                        getBestBefore()
                ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && Objects.equals(name, product.name) && Objects.equals(price, product.price) && rating == product.rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, rating);
    }
}
