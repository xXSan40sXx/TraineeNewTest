package com.akoz.shop.common;

import com.akoz.shop.entity.Category;
import com.akoz.shop.entity.Product;
import com.akoz.shop.entity.ProductStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author akoz
 */
public final class ShopUtils {

    private ShopUtils() {}

    public static void changeProductsStatus(List<Product> products, ProductStatus status) {
        for (Product product : products) {
            product.setStatus(status);
        }
    }

    public static List<Product> getProductsFromCategories(List<Category> categories) {
        List<Product> products = new ArrayList<>();

        for (Category category : categories) {
            products.addAll(category.getProducts());
        }

        return products;
    }
}
