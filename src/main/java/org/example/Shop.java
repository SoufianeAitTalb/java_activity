package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class Shop {

    public static void main(String[] args) {

        ProductManager pm = new ProductManager();

        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);


        pm.printProductReport();

        pm.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cup of tea");


        pm.printProductReport();


//        System.out.println(p1);


//
//        Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
//        System.out.println(p2);
//
//        Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FOUR_STAR, LocalDate.now());
//
//        System.out.println(p3);
//
//
//        Product p4 = pm.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR, LocalDate.now());
//
//        System.out.println(p4);
//
//        Product p5 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.THREE_STAR, LocalDate.now().plusDays(2));
//
//        System.out.println(p5);
//
//
//        Product p8 = p4.applyRating(Rating.FIVE_STAR);
//
//        Product p9 = p1.applyRating(Rating.THREE_STAR);
//
//        System.out.println(p8);
//
//
//        System.out.println(p9);
//
//
//        System.out.println(p3.getBestBefore());
//
//        System.out.println(p1.getBestBefore());


    }

    public static boolean isParagism(String mot1, String mot2) {
        char[] mot1Array = mot1.toCharArray();
        char[] mot2Array = mot2.toCharArray();
        Arrays.sort(mot1Array);
        Arrays.sort(mot2Array);

        return Arrays.equals(mot1Array, mot2Array);
    }

}
