package com.akoz.shop.persistence;

import com.akoz.shop.common.JdbcUtils;
import com.akoz.shop.entity.Category;
import com.akoz.shop.entity.Shop;
import com.akoz.shop.persistence.exception.ShopException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author akoz
 */
public enum ShopDAO {
    /**
     * Singleton instance
     */
    INSTANCE;

    private static final String SQL_FIND_ALL_SHOPS = "SELECT id, name FROM shop";
    private static final String SQL_FIND_SHOP_BY_ID = "SELECT id, name FROM shop WHERE id = ?";
    private static final String SQL_ADD_SHOP = "INSERT INTO shop (name) VALUES (?)";
    private static final String SQL_SAVE_SHOP = "UPDATE shop SET name = ? WHERE id = ?";

    private CategoryDAO categoryDAO = CategoryDAO.INSTANCE;

    public List<Shop> findAllShops() throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL_SHOPS);

            List<Shop> shops = prepareShops(resultSet);
            for (Shop shop : shops) {
                shop.setCategories(categoryDAO.findCategoriesByShopId(shop.getId()));
            }

            return shops;
        } catch (SQLException e) {
            throw new ShopException("Can't find all shops", e);
        }
    }

    public Shop findShopsById(int shopId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_SHOP_BY_ID);
            preparedStatement.setInt(1, shopId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            Shop shop = prepareShop(resultSet);
            shop.setCategories(categoryDAO.findCategoriesByShopId(shop.getId()));

            return shop;
        } catch (SQLException e) {
            throw new ShopException("Can't find shop by id = " + shopId, e);
        }
    }

    public Shop addShop(Shop shop) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_SHOP, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, shop.getName());

            preparedStatement.execute();

            Integer generatedKey = JdbcUtils.getGeneratedKey(preparedStatement);
            if (generatedKey != null) {
                shop.setId(generatedKey);
            }
        } catch (SQLException e) {
            throw new ShopException("Can't add shop " + shop, e);
        }

        for (Category category : shop.getCategories()) {
            categoryDAO.addCategory(category);
        }

        return shop;
    }

    public Shop saveShop(Shop shop) throws ShopException {
        if (!isShopExists(shop.getId())) {
            return addShop(shop);
        }

        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_SHOP);

            int index = 1;
            preparedStatement.setString(index++, shop.getName());
            preparedStatement.setInt(index++, shop.getId());

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new ShopException("Can't update shop " + shop, e);
        }

        for (Category category : shop.getCategories()) {
            categoryDAO.saveCategory(category);
        }

        return shop;
    }

    private boolean isShopExists(int shopId) throws ShopException {
        try (Connection connection = ConnectionManager.INSTANCE.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_SHOP_BY_ID);
            preparedStatement.setInt(1, shopId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new ShopException("Can't check shop existence by id = " + shopId, e);
        }
    }

    private List<Shop> prepareShops(ResultSet resultSet) throws SQLException {
        List<Shop> shops = new ArrayList<>();

        while (resultSet.next()) {
            shops.add(prepareShop(resultSet));
        }

        return shops;
    }

    private Shop prepareShop(ResultSet resultSet) throws SQLException {
        Shop shop = new Shop();

        shop.setId(resultSet.getInt("id"));
        shop.setName(resultSet.getString("name"));

        return shop;
    }
}
