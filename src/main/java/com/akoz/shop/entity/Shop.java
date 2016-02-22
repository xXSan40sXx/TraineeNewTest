package com.akoz.shop.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akoz
 */
public class Shop implements Serializable {

    private static final long serialVersionUID = 1061085532847952280L;

    private int id;
    private String name;
    private List<Category> categories = new ArrayList<>();

    public Shop() {
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        for (Category category : categories) {
            products.addAll(category.getProducts());
        }

        return products;
    }

    //----- Getters and Setters -----

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shop)) return false;

        Shop shop = (Shop) o;

        if (id != shop.id) return false;
        if (name != null ? !name.equals(shop.name) : shop.name != null) return false;
        return categories != null ? categories.equals(shop.categories) : shop.categories == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Shop{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", categories=").append(categories);
        sb.append('}');
        return sb.toString();
    }
}
