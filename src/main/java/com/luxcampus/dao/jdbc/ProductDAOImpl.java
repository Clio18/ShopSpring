package com.luxcampus.dao.jdbc;

import com.luxcampus.dao.ProductDAO;
import com.luxcampus.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
product_id serial PRIMARY KEY,
        name VARCHAR ( 50 ) NOT NULL,
        price DOUBLE PRECISION NOT NULL,
        created_on TIMESTAMP NOT NULL
        );
*/


public class ProductDAOImpl implements ProductDAO {
    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();
    private static final String GET_SQL = "SELECT product_id, name, price, created_on FROM product;";
    private static final String SAVE_SQL = "INSERT INTO product(name, price, created_on) VALUES (?, ?, ?)";
    private static final String DELETE_SQL = "DELETE FROM product where product_id= ?";
    private static final String UPDATE_SQL = "UPDATE product SET name = ?, price = ?, created_on = ? where product_id = ?";
    private static final String GET_BY_ID_SQL = "SELECT * FROM product where product_id = ?;";


    @Override
    public List<Product> get() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<Product> solutions = new ArrayList<>();
            while (resultSet.next()) {
                Product product = PRODUCT_ROW_MAPPER.mapRow(resultSet);
                solutions.add(product);
            }
            return solutions;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(Product product) {
        boolean flag;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(product.getCreated_on()));
            preparedStatement.executeUpdate();
            flag = true;
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error with product insert", e);
        }
    }

    @Override
    public boolean delete(int id) {
        boolean flag;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            if (result==0){
                throw new RuntimeException();
            }
            flag = true;
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error with product delete", e);
        }
    }

    //"UPDATE product SET name = ?, price = ?, date = ? where product_id = ?";
    @Override
    public boolean update(Product product) {
        boolean flag;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(product.getCreated_on()));
            preparedStatement.setInt(4, product.getId());
            int result = preparedStatement.executeUpdate();
            if (result==0){
                throw new RuntimeException();
            }
            flag = true;
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error with product update", e);
        }
    }

    @Override
    public Product getById(int id) {
        Product product = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                product = PRODUCT_ROW_MAPPER.mapRow(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Error with product retrieving", e);
        }
        return product;
    }


    private Connection getConnection() throws SQLException {
        Connection connection;
        String jdbc_url = System.getenv("JDBC_DATABASE_URL");
        String user = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
        if(jdbc_url!=null){
            connection = DriverManager.getConnection(jdbc_url, user, password);
        }else {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:8200/postgres", "postgres", "postgres");
        }
        return connection;
    }
}
