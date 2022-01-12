package com.luxcampus.dao.jdbc;

import com.luxcampus.dao.UserDAO;
import com.luxcampus.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private static final UserRowMapper USER_ROW_MAPPER = new UserRowMapper();
    private static final String GET_ALL_SQL = "SELECT * FROM users;";
    private static final String GET_ID_BY_EMAIL_AND_PASSWORD_SQL = "SELECT id FROM users where email = ? and password = ?;";
    private static final String GET_BY_ID_SQL = "SELECT * FROM users where id = ?;";
    private static final String SAVE_SQL = "INSERT INTO users(name, last_Name, email, password) VALUES (?, ?, ?, ?)";
    private static final String DELETE_SQL = "DELETE FROM users where id= ?";
    private static final String UPDATE_SQL = "UPDATE users SET email = ?, password = ? where id = ?";

    @Override
    public List<User> getAll() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = USER_ROW_MAPPER.mapRow(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getIdByEmailAndPassword(String email, String password) {
        int id = 0;
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ID_BY_EMAIL_AND_PASSWORD_SQL)) {
          preparedStatement.setString(1, email);
          preparedStatement.setString(2, password);
          ResultSet resultSet = preparedStatement.executeQuery();
          if (resultSet.next()){
              id = resultSet.getInt("id");
          }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Error with user retrieving", e);
        }
        return id;
    }

    @Override
    public boolean save(User user) {
        boolean flag;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            flag = true;
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error with user insert", e);
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
            throw new RuntimeException("Error with user delete", e);
        }
    }

    //"UPDATE users SET email = ?, password = ? where id = ?";
    @Override
    public boolean update(User user) {
        boolean flag;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            int result = preparedStatement.executeUpdate();
            if (result==0){
                throw new RuntimeException();
            }
            flag = true;
            return flag;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error with user update", e);
        }
    }

    @Override
    public User getById(int id) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                user = USER_ROW_MAPPER.mapRow(resultSet);
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("Error with user retrieving", e);
        }
        return user;
    }


    private Connection getConnection() throws SQLException {
        //connection in prod requires usage of environment variables
        Connection connection;
        String jdbc_url = System.getenv("JDBC_DATABASE_URL");
        String user = System.getenv("JDBC_DATABASE_USERNAME");
        String password = System.getenv("SPRING_DATASOURCE_PASSWORD");
        if(jdbc_url!=null){
            connection = DriverManager.getConnection(jdbc_url, user, password);
        }else {
            //connection in local
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:8200/postgres", "postgres", "postgres");
        }
        return connection;
    }
}
