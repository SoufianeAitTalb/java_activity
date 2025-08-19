package org.example;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            log.append(pm.getDiscounts(languageTag).entrySet().stream().map(entry -> entry.getKey() + "\t" + entry.getValue()).collect(Collectors.joining("\n")));
            Product product = pm.reviewProduct(productId, Rating.FOUR_STAR, "Yet another review");
            log.append((product != null) ? "\nProduct " + productId + " reviewed \n" : "\nProduct " + productId + " not reviewed \n");
            pm.printProductReport(productId, languageTag, clientId);
            log.append(clientId + " generated report for " + productId + " product");


            return log.toString();
        };

        List<Callable<String>> clients = Stream.generate(() -> client).limit(5).collect(Collectors.toList());
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        try {
            List<Future<String>> results = executorService.invokeAll(clients);
            executorService.shutdown();
            results.stream().forEach(result -> {
                try {
                    System.out.println(result.get());
                } catch (InterruptedException | ExecutionException e) {
                    Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error retrieving client log", e);
                }
            });
        } catch (InterruptedException e) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, "Error invoking clients", e);
        }


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
