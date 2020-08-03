package com.app.service.service;

import com.app.persistence.converter.JsonPurchasesConverter;
import com.app.persistence.enums.Category;
import com.app.persistence.model.Client;
import com.app.persistence.model.Product;
import com.app.persistence.model.Purchase;
import com.app.service.exceptions.PurchaseServiceException;
import org.eclipse.collections.impl.collector.Collectors2;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PurchaseService {
    private Map<Client, Map<Product, Long>> purchases;

    public PurchaseService(String... filename) {
        this.purchases = init(filename);
    }

    private Map<Client, Map<Product, Long>> init(String... filename) {
        return mergeJsonLists(filename).stream()
                .collect(Collectors.groupingBy(
                        Purchase::getClient,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                Collections::unmodifiableList
                        )
                )).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        p -> p.getValue().stream()
                                .flatMap(k -> k.getProducts().stream())
                                .collect(Collectors.toList())
                )).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        p -> p.getValue().stream()
                                .collect(Collectors.groupingBy(
                                        Function.identity(),
                                        Collectors.counting()
                                ))
                ));
    }

    private List<Purchase> mergeJsonLists(String... filename) {
        return Arrays.stream(filename)
                .map(f -> new JsonPurchasesConverter(f).fromJson().orElseThrow())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Client whoSpentTheMost() {
        return purchases.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        p -> calculateValueOfPurchase(p.getValue())
                )).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    public Map<Category, BigDecimal> averagePriceOfProductsInCategories() {
        return purchases
                .entrySet()
                .stream()
                .map(p -> p.getValue().keySet())
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        prod -> prod.getCategory(),
                        Collectors.collectingAndThen(
                                Collectors2.summarizingBigDecimal(prod -> prod.getPrice()),
                                product -> product.getAverage())
                        )
                );
    }

    public Map<Category, Product> theMostValuableProductInCategories() {
        return purchases
                .entrySet()
                .stream()
                .map(e -> e.getValue().keySet())
                .flatMap(prods -> prods.stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Product::getPrice)),
                                Optional::orElseThrow
                        )
                ));
    }

    public Map<Category, Product> theLeastValuableProductInCategories() {
        return purchases
                .entrySet()
                .stream()
                .map(purchases -> purchases.getValue().keySet())
                .flatMap(products -> products.stream())
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.collectingAndThen(
                                Collectors.minBy(Comparator.comparing(Product::getPrice)),
                                productOP -> productOP.orElseThrow()
                        )
                ));
    }

    public String showAllPurchases() {
        return purchases
                .entrySet()
                .stream()
                .map(p -> p.getKey() + " => " + p.getValue())
                .collect(Collectors.joining("\n"));
    }

    public Map<Client, BigDecimal> ifHasEnoughCashToCompletePurchase() {
        return purchases
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        p -> p.getKey(),
                        p -> p.getKey().getCash().subtract(calculateValueOfPurchase(p.getValue()))
                ));
    }

    private BigDecimal calculateValueOfPurchase(Map<Product, Long> purchase) {
        return purchase
                .entrySet()
                .stream()
                .map(p -> p.getKey().getPrice().multiply(BigDecimal.valueOf(p.getValue())))
                .reduce(BigDecimal::add).orElseThrow();
    }

    public Map<Integer, Category> mostPopularCategoryInCustomerAges() {
        return purchases
                .entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        e -> e.getKey().getAge(),
                        Collectors.collectingAndThen(
                                Collectors.flatMapping(e -> e.getValue()
                                        .entrySet()
                                        .stream()
                                        .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream()), Collectors.toList()),
                                products -> products
                                        .stream()
                                        .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()))
                                        .entrySet()
                                        .stream()
                                        .max(Comparator.comparing(Map.Entry::getValue))
                                        .orElseThrow()
                                        .getKey()
                        )
                ));
    }

    public Map<Category, Client> categoriesAndClients() {
        return purchases
                .entrySet()
                .stream()
                .flatMap(e -> e.getValue()
                        .entrySet()
                        .stream()
                        .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey().getCategory()).stream()))
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        category -> purchases
                                .entrySet()
                                .stream()
                                .collect(Collectors.toMap(
                                        e -> e.getKey(),
                                        e -> e.getValue().entrySet()
                                                .stream()
                                                .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream())
                                                .filter(p -> p.getCategory().equals(category))
                                                .map(Product::getPrice)
                                                .reduce(BigDecimal.ZERO, BigDecimal::add)))
                                .entrySet()
                                .stream()
                                .max(Comparator.comparing(Map.Entry::getValue))
                                .orElseThrow()
                                .getKey()
                ));
    }

    public Client whoSpentTheMostInCategory(Category category) {
        if (category == null) {
            throw new PurchaseServiceException("Category is null");
        }

        return purchases
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e
                                .getValue()
                                .entrySet()
                                .stream()
                                .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream())
                                .filter(p -> p.getCategory().equals(category))
                                .map(Product::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow()
                .getKey();
    }
}