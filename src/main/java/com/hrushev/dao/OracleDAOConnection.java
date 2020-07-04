package com.hrushev.dao;

import com.hrushev.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class OracleDAOConnection implements DAOConnection{
    private static OracleDAOConnection oracleDAOConnection;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private OracleDAOConnection() {
        super();
    }

    public static OracleDAOConnection getInstance() {
        if (oracleDAOConnection != null) {
            return oracleDAOConnection;
        } else {
            oracleDAOConnection = new OracleDAOConnection();
            return oracleDAOConnection;
        }
    }

    @Override
    public void createOrder(Date date, int quantity, double totalPrice, int userId) {

    }

    @Override
    public void addOrderDetails(String productName, int productQuantity, double productPrice, int productId, int orderId) {

    }

    @Override
    public Product findProduct(int id) {
        Product product = null;

        try {
            connection = DBConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = " + id);

            while (resultSet.next()) {
                product = parseProduct(resultSet);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    @Override
    public List<Product> getAllProducts() {

        List<Product> productList = new ArrayList<>();

        try {
            connection = DBConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM PRODUCTS");

            while (resultSet.next()) {
                productList.add(parseProduct(resultSet));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return productList;
    }

    @Override
    public List<Product> getFilteredProducts(String price, String quantity, String category) throws SQLException {
        connection = DBConnectionPool.getConnection();

        List<Product> productList = new ArrayList<>();

        String query;

        if (!category.toLowerCase().equals("default")) {
            String[] categoryList = category.split(",");

            for (int i = 0; i < categoryList.length; i++) {
                categoryList[i] = "'" + categoryList[i] + "'";
            }


            query = "SELECT * FROM PRODUCTS INNER JOIN CATEGORIES CAT on PRODUCTS.CATEGORY_ID = CAT.CATEGORY_ID WHERE CAT.CATEGORY_NAME IN (" + Arrays.toString(categoryList) + ")";

            query = query.replace("[", "");
            query = query.replace("]", "");
        } else {
            query = "SELECT * FROM PRODUCTS";
        }

        boolean sortedQuery = false;

        if (!price.toLowerCase().equals("default")) {
            query += price.toLowerCase().equals("high to low") ? " ORDER BY PRODUCT_PRICE desc" : " ORDER BY PRODUCT_PRICE asc";
            sortedQuery = true;
        }
        if (!quantity.toLowerCase().equals("default")) {
            if (sortedQuery) {
                query += quantity.toLowerCase().equals("high to low") ? ", PRODUCT_QUANTITY desc" : ", PRODUCT_QUANTITY asc";
            } else {
                query += quantity.toLowerCase().equals("high to low") ? " ORDER BY PRODUCT_QUANTITY desc" : " ORDER BY PRODUCT_QUANTITY asc";
            }
        }

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                productList.add(parseProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    private Product parseProduct(ResultSet resultSet) {
        try {
            int id = resultSet.getInt("PRODUCT_ID");
            String name = resultSet.getString("PRODUCT_NAME");
            double price = resultSet.getDouble("PRODUCT_PRICE");
            String description = resultSet.getString("PRODUCT_DESCRIPTION");
            int quantity = resultSet.getInt("PRODUCT_QUANTITY");
            int categoryId = resultSet.getInt("CATEGORY_ID");

            return new Product(id, name, price, description, quantity, categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
