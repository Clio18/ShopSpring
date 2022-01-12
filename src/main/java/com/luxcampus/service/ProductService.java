package com.luxcampus.service;

import com.luxcampus.dao.ProductDAO;
import com.luxcampus.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public class ProductService {
    ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> get() {
        List<Product> products = productDAO.get();
        System.out.println("Products in shop: " + products.size());
        return products;
    }

    public boolean save(Product product){
        product.setCreated_on(LocalDateTime.now());
        return productDAO.save(product);
    }

    public boolean delete(int id){
        return productDAO.delete(id);
    }

    public boolean update(Product product){
        product.setCreated_on(LocalDateTime.now());
        return productDAO.update(product);
    }

    public Product getById(int id) {
        Product product = productDAO.getById(id);
        return product;
    }

}
