package com.luxcampus.dao.jdbc;

import com.luxcampus.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper {
    public User mapRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String lastName = resultSet.getString("last_Name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");

        User user = User.builder().
                id(id)
                .name(name)
                .lastName(lastName)
                .email(email)
                .password(password)
                .build();

        return user;
    }
}
