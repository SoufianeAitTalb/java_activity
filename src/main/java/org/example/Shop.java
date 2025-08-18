package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Shop {

    public static void main(String[] args) {
        ProductManager pm = ProductManager.getInstance();
        AtomicInteger clientCount = new AtomicInteger(0);
        Callable<String> client = () -> {
            String clientId = "Client " + clientCount.incrementAndGet();
            String threadName = Thread.currentThread().getName();
            int productId = ThreadLocalRandom.current().nextInt(63) + 101;
            String languageTag = ProductManager.getSupportedLocales().stream().skip(ThreadLocalRandom.current().nextInt(4)).findFirst().get();
            StringBuilder log = new StringBuilder();
            log.append(clientId + " " + threadName + "\n-\tstart of log\t-\n");
            //calls will be add here
            log.append("\n-\tend of log\t-\n");

            return "";
        };


    }

//    public static boolean isParagism(String mot1, String mot2) {
//        char[] mot1Array = mot1.toCharArray();
//        char[] mot2Array = mot2.toCharArray();
//        Arrays.sort(mot1Array);
//        Arrays.sort(mot2Array);
//
//        return Arrays.equals(mot1Array, mot2Array);
//    }

}
