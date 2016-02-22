package com.akoz.shop.persistence;

import com.akoz.shop.common.JdbcUtils;
import com.akoz.shop.entity.Category;
import com.akoz.shop.entity.Product;
import com.akoz.shop.persistence.exception.ShopException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akoz
 */
public enum CategoryDAO {
    /**
     * Singleton instance
     */
    INSTANCE;

    private static final String SQL_FIND_CATEGORY_BY_ID = "SELECT id, title, shop_id FROM category WHERE id = ?";
    private static final String SQL_FIND_CATEGORY_BY_SHOP_ID = "SELECT id, title, shop_id FROM category WHERE shop_id = ?";
    private static final String SQL_ADD_CATEGORY = "INSERT INTO category (title, shop_id) VALUES (?, ?)";
    private static final String SQL_SAVE_CATEGORY = "UPDATE category SET title = ?, shop_id = ? WHERE id = ?";

    private ProductDAO productDAO = ProductDAO.INSTANCE;

    public Category findCategoryById(int categoryId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_CATEGORY_BY_ID);
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            Category category = prepareCategory(resultSet);
            category.setProducts(productDAO.findAllProducts());

            return category;
        } catch (SQLException e) {
            throw new ShopException("Can't find category by id = " + categoryId, e);
        }
    }

    public List<Category> findCategoriesByShopId(int shopId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_CATEGORY_BY_SHOP_ID);
            preparedStatement.setInt(1, shopId);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Category> categories = prepareCategories(resultSet);
            for (Category category : categories) {
                category.setProducts(productDAO.findProductsByCategory(category.getId()));
            }

            return categories;
        } catch (SQLException e) {
            throw new ShopException("Can't find categories by shop with id = " + shopId, e);
        }
    }

    public Category addCategory(Category category) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_CATEGORY, Statement.RETURN_GENERATED_KEYS);

            int index = 1;
            preparedStatement.setString(index++, category.getTitle());
            preparedStatement.setInt(index++, category.getShopId());

            preparedStatement.execute();

            Integer generatedKey = JdbcUtils.getGeneratedKey(preparedStatement);
            if (generatedKey != null) {
                category.setId(generatedKey);
            }
        } catch (SQLException e) {
            throw new ShopException("Can't add category " + category, e);
        }

        for (Product product : category.getProducts()) {
            productDAO.addProduct(product);
        }

        return category;
    }

    public Category saveCategory(Category category) throws ShopException {
        if (!isCategoryExists(category.getId())) {
            return addCategory(category);
        }

        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_CATEGORY);

            int index = 1;
            preparedStatement.setString(index++, category.getTitle());
            preparedStatement.setInt(index++, category.getShopId());
            preparedStatement.setInt(index++, category.getId());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ShopException("Can't save category" + category, e);
        }

        for (Product product : category.getProducts()) {
            productDAO.saveProduct(product);
        }

        return category;
    }

    private boolean isCategoryExists(int categoryId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_CATEGORY_BY_ID);
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new ShopException("Can't check category existence by id = " + categoryId, e);
        }
    }
    private List<Category> prepareCategories(ResultSet resultSet) throws SQLException {
        List<Category> categories = new ArrayList<>();

        while (resultSet.next()) {
            categories.add(prepareCategory(resultSet));
        }

        return categories;
    }

    private Category prepareCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();

        category.setId(resultSet.getInt("id"));
        category.setTitle(resultSet.getString("title"));
        category.setShopId(resultSet.getInt("shop_id"));

        return category;
    }
}
