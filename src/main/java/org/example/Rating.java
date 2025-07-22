package org.example;

public enum Rating {


    NOT_RATED("Not Rated"),
    ONE_STAR("1 Star"),
    TWO_STAR("2 Stars"),
    THREE_STAR("3 Stars"),
    FOUR_STAR("4 Stars"),
    FIVE_STAR("5 Stars");

    private final String stars;


    Rating(String stars) {
        this.stars = stars;
    }


    public String getStars() {
        return stars;
    }
}
