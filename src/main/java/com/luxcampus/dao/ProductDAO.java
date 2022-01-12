package com.luxcampus.dao;

import com.luxcampus.entity.Product;

import java.util.List;

public interface ProductDAO {
    List<Product> get();

    boolean save(Product product);

    boolean delete(int id);

    boolean update(Product product);

    Product getById(int id);
}
