package com.akoz.shop.runner;

import com.akoz.shop.common.ShopUtils;
import com.akoz.shop.entity.Category;
import com.akoz.shop.entity.Product;
import com.akoz.shop.entity.Shop;
import com.akoz.shop.persistence.ShopDAO;
import com.akoz.shop.persistence.exception.ShopException;

import java.util.List;

import static com.akoz.shop.entity.ProductStatus.*;

/**
 * @author akoz
 */
public class ShopTask implements Runnable {

    private Shop shop;

    public ShopTask(Shop shop) {
        this.shop = shop;
    }

    public void run() {
        List<Category> categories = shop.getCategories();

        if (categories.size() < 2) {
            return;
        }

        task1(categories);
        task2(categories);
        task3(shop.getProducts());

        try {
            ShopDAO.INSTANCE.saveShop(shop);
        } catch (ShopException e) {
            e.printStackTrace();
        }
    }

    private void task1(List<Category> categories) {
        Product productA = new Product("Product A", 10.0, EXPECTED);
        Product productB = new Product("Product B", 100.0, ABSENT);
        Product productC = new Product("Product C", 50.0, AVAILABLE);
        Product productD = new Product("Product C", 50.0, AVAILABLE);

        categories.get(0).addProduct(productA);
        categories.get(0).addProduct(productB);

        categories.get(1).addProduct(productC);
        categories.get(1).addProduct(productD);
    }

    private void task2(List<Category> categories) {
        ShopUtils.changeProductsStatus(categories.get(0).getProducts(), ABSENT);

        List<Category> otherCategories = categories.subList(1, categories.size());
        List<Product> otherProducts = ShopUtils.getProductsFromCategories(otherCategories);
        List<Product> halfProducts = otherProducts.subList(0, otherProducts.size() / 2);
        ShopUtils.changeProductsStatus(halfProducts, EXPECTED);
    }


    private void task3(List<Product> products) {
        for (Product product : products) {
            if (product.getStatus() == AVAILABLE) {
                product.increasePrice(20);
            }
        }
    }
}
