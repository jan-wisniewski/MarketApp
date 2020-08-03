package com.app.ui.menu;

import com.app.persistence.enums.Category;
import com.app.service.service.PurchaseService;
import com.app.ui.user_data.UserDataService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuService {

    private final PurchaseService PURCHASE_SERVICE;

    public void mainMenu() {
        while (true) {
            try {
                System.out.println("----- MAIN MENU -----");
                System.out.println("0. Exit");
                System.out.println("1. Client who paid the most for purchases");
                System.out.println("2. Client who paid the most in category");
                System.out.println("3. Most frequently purchased products and age of buyers");
                System.out.println("4. Average purchase price of products in categories");
                System.out.println("5. The cheapest products in categories");
                System.out.println("6. The most expensive products in categories");
                System.out.println("7. Clients and their debits");
                System.out.println("8. Customers who most often bought the products of the categories");
                System.out.println("9. Show all purchases");
                int decision = UserDataService.getInteger("Type correct option");
                switch (decision) {
                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    case 1 -> option1();
                    case 2 -> option2();
                    case 3 -> option3();
                    case 4 -> option4();
                    case 5 -> option5();
                    case 6 -> option6();
                    case 7 -> option7();
                    case 8 -> option8();
                    case 9 -> showAll();
                    default -> System.out.println("No option with this number!");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void option8() {
        System.out.println(PURCHASE_SERVICE.categoriesAndClients());
    }

    private void option7() {
        System.out.println(PURCHASE_SERVICE.ifHasEnoughCashToCompletePurchase());
    }

    private void option6() {
        System.out.println(PURCHASE_SERVICE.theMostValuableProductInCategories());
    }

    private void showAll() {
        System.out.println(PURCHASE_SERVICE.showAllPurchases());
    }

    private void option1() {
        System.out.println(toJson(PURCHASE_SERVICE.whoSpentTheMost()));
    }

    private void option2() {
        Category category = UserDataService.getCategory("Type category");
        System.out.println(toJson(PURCHASE_SERVICE.whoSpentTheMostInCategory(category)));
    }

    private void option3() {
        System.out.println(PURCHASE_SERVICE.mostPopularCategoryInCustomerAges());
    }

    private void option4() {
        System.out.println(PURCHASE_SERVICE.averagePriceOfProductsInCategories());
    }

    private void option5() {
        System.out.println(PURCHASE_SERVICE.theLeastValuableProductInCategories());
    }

    private <T> String toJson(T item) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(item);
    }
}