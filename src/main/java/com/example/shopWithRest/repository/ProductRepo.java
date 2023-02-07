package com.example.shopWithRest.repository;

import com.example.shopWithRest.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepo {
    private final Map<Integer, Product> products = new HashMap<>(Map.of(
            1, new Product(1, "Apfel"),
            2, new Product(2, "Banane"),
            3, new Product(3, "Zitrone"),
            4, new Product(4, "Mandarine")
    ));

    public Product getProduct(int id) {
        Product product = products.get(id);
        if (product == null) {
            throw new NoSuchElementException("No product with id " + id + " found in this product repo.");
        }
        return product;
    }

    public List<Product> listProducts() {
        return new ArrayList<>(products.values());
    }

    public void addProduct(Product product) {
        products.put(product.id(), product);
    }
}
