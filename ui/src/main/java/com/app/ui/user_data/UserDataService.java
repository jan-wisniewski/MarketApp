package com.app.ui.user_data;

import com.app.persistence.enums.Category;
import com.app.ui.exception.UserDataServiceException;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class UserDataService {

    private final static Scanner SCANNER = new Scanner(System.in);

    public static String getString(String msg) {
        System.out.println(msg);
        return SCANNER.nextLine();
    }

    public static Integer getInteger(String msg) {
        System.out.println(msg);
        String value = SCANNER.nextLine();
        if (!value.matches("\\d+")) {
            throw new UserDataServiceException("Incorrect value");
        }
        return Integer.parseInt(value);
    }

    public static Category getCategory(String msg) {
        System.out.println(msg);
        Category[] categories = Category.values();
        AtomicInteger counter = new AtomicInteger();
        String categoriesList = Arrays.stream(categories)
                .map(cat -> counter.incrementAndGet() + ". " + cat.toString())
                .collect(Collectors.joining("\n"));
        int decision;
        do {
            System.out.println(categoriesList);
            decision = getInteger("Type category id");
        } while (decision <= 0 || decision > categories.length);
        return categories[decision - 1];
    }

}
