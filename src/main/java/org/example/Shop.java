package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class Shop {

    public static void main(String[] args) {

        ProductManager pm = new ProductManager("en-GB");


        pm.printProductReport(101);

        pm.printProductReport(103);

        pm.createProduct(164, "Kombucha", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pm.reviewProduct(164, Rating.TWO_STAR, "Looks like tea but is it?");
        pm.reviewProduct(164, Rating.FOUR_STAR, "Fine tea");
        pm.reviewProduct(164, Rating.FOUR_STAR, "This is not tea");
        pm.reviewProduct(164, Rating.FIVE_STAR, "Perfect");
        pm.printProductReport(164);


        pm.printProducts(p -> p.getPrice().floatValue() < 2, (pOne, pTwo) -> pTwo.getRating().ordinal() - pOne.getRating().ordinal());

    }

    public static boolean isParagism(String mot1, String mot2) {
        char[] mot1Array = mot1.toCharArray();
        char[] mot2Array = mot2.toCharArray();
        Arrays.sort(mot1Array);
        Arrays.sort(mot2Array);

        return Arrays.equals(mot1Array, mot2Array);
    }

}
