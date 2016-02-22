package com.akoz.shop.entity;

import java.io.Serializable;

/**
 * @author akoz
 */
public class Product implements Serializable {

    private static final long serialVersionUID = -6486616149754360826L;

    private int id;
    private String title;
    private double price;
    private ProductStatus status = ProductStatus.EXPECTED;
    private int categoryId;

    public Product() {
    }

    public Product(String title, double price, ProductStatus status) {
        this.title = title;
        this.price = price;
        this.status = status;
    }

    public void increasePrice(double percent) {
        price += price * percent / 100;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (Double.compare(product.price, price) != 0) return false;
        if (categoryId != product.categoryId) return false;
        if (title != null ? !title.equals(product.title) : product.title != null) return false;
        return status == product.status;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + categoryId;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", categoryId=").append(categoryId);
        sb.append('}');
        return sb.toString();
    }
}
