package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

public class Shop {

    public static void main(String[] args) {

        ProductManager pm = new ProductManager(Locale.UK);

        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED, LocalDate.now());


        pm.printProductReport(p1);

        pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");
        pm.reviewProduct(p1, Rating.TWO_STAR, "Rather weak tea");
        pm.reviewProduct(p1, Rating.FOUR_STAR, "Fine tea");
        pm.reviewProduct(p1, Rating.FOUR_STAR, "Good tea");
        pm.reviewProduct(p1, Rating.FIVE_STAR, "Perfect tea");
        pm.reviewProduct(p1, Rating.THREE_STAR, "Just add some lemon");


        pm.printProductReport(p1    );



    }

    public static boolean isParagism(String mot1, String mot2) {
        char[] mot1Array = mot1.toCharArray();
        char[] mot2Array = mot2.toCharArray();
        Arrays.sort(mot1Array);
        Arrays.sort(mot2Array);

        return Arrays.equals(mot1Array, mot2Array);
    }

}
