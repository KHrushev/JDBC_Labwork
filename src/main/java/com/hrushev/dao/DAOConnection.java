package com.hrushev.dao;

import com.hrushev.model.Product;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface DAOConnection {
    void createOrder(Date date, int quantity, double totalPrice, int userId);
    void addOrderDetails(String productName, int productQuantity, double productPrice, int productId, int orderId);
    Product findProduct(int id) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    List<Product> getFilteredProducts(String price, String quantity, String category) throws SQLException;
}
