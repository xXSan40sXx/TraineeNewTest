package com.akoz.shop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akoz
 */
public class Category implements Serializable {

    private static final long serialVersionUID = -2818897244733431235L;

    private int id;
    private String title;
    private List<Product> products = new ArrayList<>();
    private int shopId;

    public Category() {
    }

    //----- Getters and Setters -----

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public void addProduct(Product product) {
        product.setCategoryId(id);
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        if (shopId != category.shopId) return false;
        if (title != null ? !title.equals(category.title) : category.title != null) return false;
        return products != null ? products.equals(category.products) : category.products == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (products != null ? products.hashCode() : 0);
        result = 31 * result + shopId;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Category{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", products=").append(products);
        sb.append(", shopId=").append(shopId);
        sb.append('}');
        return sb.toString();
    }
}
