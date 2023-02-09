package com.example.shopWithRest.controller;

import com.example.shopWithRest.model.Order;
import com.example.shopWithRest.model.Product;
import com.example.shopWithRest.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping("products")
    public List<Product> listProducts() {
        return shopService.listProducts();
    }

    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable String id) {
        return shopService.getProduct(id);
    }

    @PostMapping("products/new")
    public Product addProduct(@RequestBody Product product) {
        return shopService.addProduct(product);
    }

    @DeleteMapping("products/{id}")
    public List<Order> deleteProduct(@PathVariable String id) {
        return shopService.deleteProduct(id);
    }

    @GetMapping("orders")
    public List<Order> listOrders() {
        return shopService.listOrders();
    }

    @GetMapping("orders/{id}")
    public Order getOrder(@PathVariable String id) {
        return shopService.getOrder(id);
    }

    @PostMapping("orders/new")
    public Order addOrder(@RequestBody List<String> productIds) {
        return shopService.addOrder(productIds);
    }

    @DeleteMapping("orders/{id}")
    public Order deleteOrder(@PathVariable String id) {
        return shopService.deleteOrder(id);
    }

    @PostMapping("orders/newById/{id}")
    public Order addOrderById(@PathVariable String id) {
        return shopService.addOrderById(id);
    }
}
