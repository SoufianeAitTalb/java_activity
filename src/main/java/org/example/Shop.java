package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class Shop {

    public static void main(String[] args) {

        ProductManager pm = new ProductManager("en-GB");

        Product p1 = pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED, LocalDate.now());


//        pm.printProductReport(101);

        pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
        pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
        pm.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
        pm.reviewProduct(101, Rating.FIVE_STAR, "Perfect tea");
        pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");


//        pm.printProductReport(101);


        Product p2 = pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.NOT_RATED);

        pm.reviewProduct(102, Rating.THREE_STAR, "Coffee was ok");
        pm.reviewProduct(102, Rating.ONE_STAR, "Where is the milk?!");
        pm.reviewProduct(102, Rating.FIVE_STAR, "It's perfect with ten spoons of sugar");
//        pm.changeLocale("ru-RU");
//        pm.printProductReport(102);

        Product p3 = pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.NOT_RATED, LocalDate.now().plusDays(2));

        pm.reviewProduct(103, Rating.FIVE_STAR, "Very nice cake");

        pm.reviewProduct(103, Rating.FOUR_STAR, "Good, but I've expected more chocolate");
        pm.reviewProduct(103, Rating.FIVE_STAR, "This cake is perfect!");
//        pm.printProductReport(103);
        Product p4 = pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.99), Rating.NOT_RATED, LocalDate.now()
        );
        pm.reviewProduct(104, Rating.THREE_STAR, "Just another cookie");
        pm.reviewProduct(104, Rating.THREE_STAR, "Ok");
//        pm.printProductReport(104);

        Product p5 = pm.createProduct(105, "Hot Chocolate",
                BigDecimal.valueOf(2.50), Rating.NOT_RATED);
        pm.reviewProduct(105, Rating.FOUR_STAR, "Tasty!");
        pm.reviewProduct(105, Rating.FOUR_STAR, "No bad at all");
//        pm.printProductReport(105);

        Product p6 = pm.createProduct(106, "Chocolate",
                BigDecimal.valueOf(2.50), Rating.NOT_RATED,
                LocalDate.now().plusDays(3));
        pm.reviewProduct(106, Rating.TWO_STAR, "Too sweet");
        pm.reviewProduct(106, Rating.THREE_STAR, "Better then cookie");
        pm.reviewProduct(106, Rating.TWO_STAR, "Too bitter");
        pm.reviewProduct(106, Rating.ONE_STAR, "I don't get it!");
//        pm.printProductReport(106);


        pm.printProducts((pOne, pTwo) -> pTwo.getRating().ordinal() - pOne.getRating().ordinal());

        pm.printProducts((pOne,pTwo)-> pTwo.getPrice().compareTo(pOne.getPrice()));

        Comparator<Product> ratingSorter = (pOne,pTwo)-> pTwo.getRating().ordinal() - pOne.getRating().ordinal();
        Comparator<Product> priceSorter = (pOne,pTwo)-> (pTwo.getPrice().compareTo(pOne.getPrice()));

        pm.printProducts(ratingSorter.thenComparing(priceSorter));
        pm.printProducts(ratingSorter.thenComparing(priceSorter).reversed());

    }

    public static boolean isParagism(String mot1, String mot2) {
        char[] mot1Array = mot1.toCharArray();
        char[] mot2Array = mot2.toCharArray();
        Arrays.sort(mot1Array);
        Arrays.sort(mot2Array);

        return Arrays.equals(mot1Array, mot2Array);
    }

}
