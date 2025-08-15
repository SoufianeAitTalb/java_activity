package org.example;

import java.awt.color.ProfileDataException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {


    private Map<Product, List<Review>> products = new HashMap<>();


    private static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;

            resources = ResourceBundle.getBundle("org.example.resources", locale);

            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);

            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            String type = (product instanceof Food) ? resources.getString("food") : resources.getString("drink");
            return MessageFormat.format(resources.getString("product"), product.getName(), moneyFormat.format(product.getPrice()), product.getRating().getStars(), dateFormat.format(product.getBestBefore()), type);
        }

        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"), review.rating().getStars(), review.comments());
        }

        private String getText(String key) {
            return resources.getString(key);
        }

    }

    private final ResourceBundle config = ResourceBundle.getBundle("config");
    private final MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private final MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private final Path reportsFolder = Path.of(config.getString("reports.folder"));
    private final Path dataFolder = Path.of(config.getString("data.folder"));
    private final Path tempFolder = Path.of(config.getString("temp.folder"));
//    private ResourceFormatter formatter;


    private static final Map<String, ResourceFormatter> formatters = Map.of(
            "en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US),
            "ru-RU", new ResourceFormatter(new Locale("ru", "RU")),
            "fr-FR", new ResourceFormatter(Locale.FRANCE),
            "zh-CN", new ResourceFormatter(Locale.CHINA)

    );

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private static final ProductManager pm = new ProductManager();


//    public ProductManager(Locale locale) {
//        this(locale.toLanguageTag());
//    }

    private ProductManager() {
//        changeLocale(languageTag);
        loadAllData();
    }


    public ResourceFormatter changeLocale(String languageTag) {
        return formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }


    public static ProductManager getInstance() {
        return pm;
    }


    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());

        return product;
    }

    public Product findProduct(int id) throws ProductManagerException {


        return products.keySet().stream().filter(product -> product.getId() == id).findFirst().orElseThrow(() -> new ProductManagerException("Product with id " + id + " not found"));
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
            return null;
        }
    }


    public Product reviewProduct(Product product, Rating rating, String comments) {

        List<Review> reviews = products.get(product);


        reviews.add(new Review(rating, comments));
//        int sum = 0;
//
//
//        for (Review review : reviews) {
//            sum += review.rating().ordinal();
//        }
//
//
//        product = product.applyRating(Reteable.convert(Math.round((float) sum / reviews.size())));


        product = product.applyRating(Reteable.convert((int) Math.round(reviews.stream().mapToInt(review -> review.rating().ordinal()).average().orElse(0))));


        products.put(product, reviews);

        return product;
    }


    public void printProductReport(int id, String languageTag) {
        try {
            printProductReport(findProduct(id), languageTag);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error printing product report " + e.getMessage(), e);
        }
    }

    public void printProductReport(Product product, String languageTag) throws IOException {


        ResourceFormatter formatter = changeLocale(languageTag);


        List<Review> reviews = products.get(product);


        Collections.sort(reviews);


        Collections.sort(reviews);
        Path productFile = reportsFolder.resolve(MessageFormat.format(config.getString("report.file"), product.getId()));


        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE), StandardCharsets.UTF_8))) {
            out.append(formatter.formatProduct(product) + System.lineSeparator());

            if (reviews.isEmpty()) {
                out.append(formatter.getText("no.reviews") + System.lineSeparator());
            } else {


                out.append(reviews.stream().map(review -> formatter.formatReview(review) + System.lineSeparator()).collect(Collectors.joining()));

            }
            System.out.println(out);
        } catch (Exception e) {
            System.out.println("Error writing product report: " + e.getMessage());
        }

        System.out.println("Product report for " + product.getName() + " saved to " + productFile.toAbsolutePath());
    }

    public void printProducts(Predicate<Product> filer, Comparator<Product> sorter, String languageTag) {
        ResourceFormatter formatter = changeLocale(languageTag);

        StringBuilder txt = new StringBuilder();

        products.keySet().stream().sorted(sorter).filter(filer).forEach(product ->
        {
            txt.append(formatter.formatProduct(product)).append('\n');
        });
        System.out.println(txt);
    }


    @SuppressWarnings("unchecked")
    private void restoreData() {
        try {

            Path tempFile = Files.list(tempFolder).filter(path -> path.getFileName().toString().endsWith("tmp")).findFirst().orElseThrow();


            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(tempFile, StandardOpenOption.DELETE_ON_CLOSE))) {
                products = (HashMap) in.readObject();
            }


        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error restoring data " + e.getMessage(), e);
        }
    }

    private void dumpData() {
        try {
            if (Files.notExists(tempFolder)) {
                Files.createDirectory(tempFolder);
            }

            Path tempFile = tempFolder.resolve(

                    MessageFormat.format(config.getString("temp.file"), Instant.now().getEpochSecond())
            );

            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tempFile, StandardOpenOption.CREATE))) {
                out.writeObject(products);
                products = new HashMap<>();
            }


        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error dumping data " + e.getMessage(), e);
        }
    }

    private void loadAllData() {
        try {
            products = Files.list(dataFolder).filter(file -> file.getFileName().startsWith("product")).map(file -> loadProduct(file)).filter(product -> product != null).collect(Collectors.toMap(product -> product, product -> loadReviews(product)));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading data " + e.getMessage(), e);
        }
    }

    private Product loadProduct(Path file) {
        Product product = null;


        try {
            product = parseProduct(Files.lines(dataFolder.resolve(file), StandardCharsets.UTF_8).findFirst().orElseThrow());
        } catch (Exception e) {

            logger.log(Level.WARNING, "Error loading product " + e.getMessage());
        }
        return product;
    }

    private List<Review> loadReviews(Product product) {
        List<Review> reviews = null;


        Path file = dataFolder.resolve(MessageFormat.format(config.getString("reviews.data.file"), product.getId()));
        if (Files.notExists(file)) {

            reviews = new ArrayList<>();
        } else {
            try {
                reviews = Files.lines(file, StandardCharsets.UTF_8).map(text -> parseReview(text)).filter(review -> review != null).collect(Collectors.toList());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error loading reviews " + e.getMessage());
            }
        }
        return reviews;
    }

    private Review parseReview(String text) {
        Review review = null;
        try {
            Object[] values = reviewFormat.parse(text);


            review = new Review(Reteable.convert(Integer.parseInt((String) values[0])), (String) values[1]);


        } catch (ParseException | NumberFormatException e) {
            logger.log(Level.WARNING, "Error parsing review " + text + " " + e.getMessage());
        }

        return review;
    }

    private Product parseProduct(String text) {
        Product product = null;
        try {
            Object[] values = productFormat.parse(text);

            int id = Integer.parseInt((String) values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            Rating rating = Reteable.convert(Integer.parseInt((String) values[4]));

            switch ((String) values[0]) {
                case "D":
                    product = new Drink(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    product = new Food(id, name, price, rating, bestBefore);
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException e) {
            logger.log(Level.WARNING, "Error parsing product " + text + " " + e.getMessage());
        }


        return product;
    }

    public Map<String, String> getDiscounts(String languageTag) {

        ResourceFormatter formatter = changeLocale(languageTag);
        return products.keySet()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                product -> product.getRating().getStars(),
                                Collectors.collectingAndThen(
                                        Collectors.summingDouble(
                                                product -> product.getDiscount().doubleValue()
                                        ),
                                        discount -> formatter.moneyFormat.format(discount)
                                )
                        )
                );
    }


}
