package com.akoz.shop.persistence;

import com.akoz.shop.common.JdbcUtils;
import com.akoz.shop.entity.Product;
import com.akoz.shop.entity.ProductStatus;
import com.akoz.shop.persistence.exception.ShopException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akoz
 */
public enum ProductDAO {
    /**
     * Singleton instance
     */
    INSTANCE;

    private static final String SQL_FIND_ALL_PRODUCTS = "SELECT id, title, price, status, category_id FROM product";
    private static final String SQL_FIND_PRODUCT_BY_ID = "SELECT id, title, price, status, category_id FROM product WHERE id = ?";
    private static final String SQL_FIND_PRODUCTS_BY_CATEGORY_ID = "SELECT id, title, price, status, category_id FROM product WHERE category_id = ?";
    private static final String SQL_ADD_PRODUCT = "INSERT INTO product (title, price, status, category_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_SAVE_PRODUCT = "UPDATE product SET title = ?, price = ?, status = ?, category_id = ? WHERE id = ?";

    public List<Product> findAllProducts() throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_PRODUCTS);

            return prepareProducts(resultSet);
        } catch (SQLException e) {
            throw new ShopException("Can't find all products", e);
        }
    }

    public Product findProductById(int productId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PRODUCT_BY_ID);
            preparedStatement.setInt(1, productId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            return prepareProduct(resultSet);
        } catch (SQLException e) {
            throw new ShopException("Can't find product by id = " + productId, e);
        }
    }

    public List<Product> findProductsByCategory(int categoryId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PRODUCTS_BY_CATEGORY_ID);
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return prepareProducts(resultSet);
        } catch (SQLException e) {
            throw new ShopException("Can't find products by category with id = " + categoryId, e);
        }
    }

    public Product addProduct(Product product) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_PRODUCT, Statement.RETURN_GENERATED_KEYS);

            int index = 1;
            preparedStatement.setString(index++, product.getTitle());
            preparedStatement.setDouble(index++, product.getPrice());
            preparedStatement.setString(index++, product.getStatus().name());
            preparedStatement.setInt(index++, product.getCategoryId());

            preparedStatement.executeUpdate();

            Integer generatedKey = JdbcUtils.getGeneratedKey(preparedStatement);
            if (generatedKey != null) {
                product.setId(generatedKey);
            }

            return product;
        } catch (SQLException e) {
            throw new ShopException("Can't add product " + product, e);
        }
    }


    public Product saveProduct(Product product) throws ShopException {
        if (!isProductExists(product.getId())) {
            return addProduct(product);
        }

        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_PRODUCT);

            int index = 1;
            preparedStatement.setString(index++, product.getTitle());
            preparedStatement.setDouble(index++, product.getPrice());
            preparedStatement.setString(index++, product.getStatus().name());
            preparedStatement.setInt(index++, product.getCategoryId());
            preparedStatement.setInt(index++, product.getId());

            preparedStatement.executeUpdate();

            return product;
        } catch (SQLException e) {
            throw new ShopException("Can't save product " + product, e);
        }
    }

    private boolean isProductExists(int productId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PRODUCT_BY_ID);
            preparedStatement.setInt(1, productId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new ShopException("Can't check product existence by id = " + productId, e);
        }
    }

    private List<Product> prepareProducts(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();

        while (resultSet.next()) {
            Product product = prepareProduct(resultSet);
            products.add(product);
        }

        return products;
    }

    private Product prepareProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();

        product.setId(resultSet.getInt("id"));
        product.setTitle(resultSet.getString("title"));
        product.setPrice(resultSet.getDouble("price"));
        product.setStatus(ProductStatus.valueOf(resultSet.getString("status")));
        product.setCategoryId(resultSet.getInt("category_id"));

        return product;
    }
}
