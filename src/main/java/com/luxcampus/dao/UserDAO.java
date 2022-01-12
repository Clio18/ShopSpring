package com.luxcampus.dao;

import com.luxcampus.entity.User;

import java.util.List;

public interface UserDAO {
    List<User> getAll();

    int getIdByEmailAndPassword(String email, String password);

    boolean save(User user);

    boolean delete(int id);

    boolean update(User user);

    User getById(int id);
}
