package com.example.shopWithRest.model;

import java.util.List;

public record Order(
        int id,
        List<Product> products
) {
}
