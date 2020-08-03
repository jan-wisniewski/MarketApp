package com.app.ui;

import com.app.service.service.PurchaseService;
import com.app.ui.menu.MenuService;

public class App {
    public static void main(String[] args) {
        final String PURCHASES_FILENAME1 = "./resources/data/purchases1.json";
        final String PURCHASES_FILENAME2 = "./resources/data/purchases2.json";
        final String PURCHASES_FILENAME3 = "./resources/data/purchases3.json";
        final String PURCHASES_FILENAME4 = "./resources/data/purchases4.json";
        PurchaseService purchaseService = new PurchaseService(
                PURCHASES_FILENAME1,
                PURCHASES_FILENAME2,
                PURCHASES_FILENAME3,
                PURCHASES_FILENAME4);
        MenuService menuService = new MenuService(purchaseService);
        menuService.mainMenu();
    }
}
