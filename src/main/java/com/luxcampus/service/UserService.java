package com.luxcampus.service;
import com.luxcampus.dao.UserDAO;
import com.luxcampus.entity.User;
import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAll() {
        List<User> users = userDAO.getAll();
        System.out.println("Registered users in shop: " + users.size());
        return users;
    }

    public boolean save(User user){
        return userDAO.save(user);
    }

    public boolean delete(int id){
        return userDAO.delete(id);
    }

    public boolean update(User user){
        return userDAO.update(user);
    }

    public boolean isNew(User user){
        boolean flag = false;
        int id = userDAO.getIdByEmailAndPassword(user.getEmail(), user.getPassword());
        if (id==0){
            flag = true;
        }
        return flag;
    }

}
