package com.example.shopWithRest.service;

import com.example.shopWithRest.model.Order;
import com.example.shopWithRest.model.Product;
import com.example.shopWithRest.repository.OrderRepo;
import com.example.shopWithRest.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final WebClient webClient = WebClient.create("https://my-json-server.typicode.com/Flooooooooooorian/OrderApi");
    private final IdService idService;

    public Product getProduct(String id) {
        return productRepo.getProduct(id);
    }

    public List<Product> listProducts() {
        return productRepo.listProducts();
    }

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product product = productRepo.getProduct(productId);
            products.add(product);
        }

        Order order = new Order(idService.generateId(), products);
        return orderRepo.addOrder(order);
    }

    public Order getOrder(String orderId) {
        return orderRepo.getOrder(orderId);
    }

    public List<Order> listOrders() {
        return orderRepo.listOrders();
    }

    public Order deleteOrder(String id) {
        if (getOrder(id) == null) {
            throw new NoSuchElementException("Order with id " + id + " does not exist!");
        }
        return orderRepo.deleteOrder(id);
    }

    public Product addProduct(Product product) {
        Product productWithId = new Product(idService.generateId(), product.name());
        return productRepo.addProduct(productWithId);
    }

    public List<Order> deleteProduct(String id) {
        Product productToDelete = getProduct(id);
        List<Order> affectedOrders = listOrders().stream()
                .filter(o -> o.products().contains(productToDelete))
                .toList();
        affectedOrders.stream().forEach(o -> o.products().remove(productToDelete));
        productRepo.deleteProduct(id);
        return affectedOrders;
    }

    public Order addOrderById(String id) {
        Order order = Objects.requireNonNull(webClient.get()
                .uri("/orders/" + id)
                .retrieve()
                .toEntity(Order.class)
                .block())
                .getBody();

        return orderRepo.addOrder(order);
    }
}
