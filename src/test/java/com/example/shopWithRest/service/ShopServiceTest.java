package com.example.shopWithRest.service;

import com.example.shopWithRest.model.Order;
import com.example.shopWithRest.model.Product;
import com.example.shopWithRest.repository.OrderRepo;
import com.example.shopWithRest.repository.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopServiceTest {
    ProductRepo productRepo;
    OrderRepo orderRepo;
    ShopService shopService;
    Product p1, p2;
    List<Product> smallProductList, bigProductList;
    Order o1, o2;
    List<Order> smallOrderList, bigOrderList;
    IdService idService;


    @BeforeEach
    void init() {
        // GIVEN
        p1 = new Product("1", "Product 1");
        p2 = new Product("2", "Product 2");
        smallProductList = new ArrayList<>(List.of(p1));
        bigProductList = new ArrayList<>(List.of(p1, p2));
        o1 = new Order("1", bigProductList);
        o2 = new Order("2", smallProductList);
        smallOrderList = new ArrayList<>(List.of(o1));
        bigOrderList = new ArrayList<>(List.of(o1, o2));
        productRepo = mock(ProductRepo.class);
        orderRepo = mock(OrderRepo.class);
        idService = mock(IdService.class);
        shopService = new ShopService(productRepo, orderRepo, idService);

    }

    @Test
    void getProduct() {
        // GIVEN
        Product expected = p2;
        when(productRepo.getProduct("2")).thenReturn(expected);

        // WHEN
        Product actual = shopService.getProduct("2");

        // THEN
        verify(productRepo).getProduct("2");
        assertEquals(expected, actual);
    }

    @Test
    void listProducts() {
        // GIVEN
        List<Product> expected = bigProductList;
        when(productRepo.listProducts())
                .thenReturn(bigProductList);

        // WHEN
        List<Product> actual = shopService.listProducts();

        // THEN
        verify(productRepo).listProducts();
        assertEquals(expected, actual);
    }

    @Test
    void addOrder() {
        // GIVEN
        Order expected = o2;
        when(orderRepo.addOrder(o2)).thenReturn(o2);
        when(productRepo.getProduct(p1.id())).thenReturn(p1);
        when(idService.generateId()).thenReturn(o2.id());

        // WHEN
        Order actual = shopService.addOrder(o2.products().stream().map(p -> String.valueOf(p.id())).toList());

        // THEN
        verify(orderRepo).addOrder(o2);
        assertEquals(expected, actual);

    }

    @Test
    void getOrder() {
        // GIVEN
        when(orderRepo.getOrder(o1.id())).thenReturn(o1);
        // when(orderRepo.getOrder(2)).thenReturn(o2);
        Order expected = o1;

        // WHEN
        Order actual = shopService.getOrder(o1.id());

        // THEN
        assertEquals(expected, actual);
    }

    @Test
    void listOrders() {
        // GIVEN
        List<Order> expected = bigOrderList;
        when (orderRepo.listOrders()).thenReturn(bigOrderList);

        // WHEN
        List<Order> actual = shopService.listOrders();

        // THEN
        verify(orderRepo).listOrders();
        assertEquals(expected, actual);
    }

    @Test
    @DirtiesContext
    void deleteOrder() {
        // GIVEN
        when(orderRepo.deleteOrder(o1.id())).thenReturn(o1);
        when(orderRepo.getOrder(o1.id())).thenReturn(o1);
        Order expected = o1;

        // WHEN
        Order actual = shopService.deleteOrder(o1.id());

        // THEN
        assertEquals(expected, actual);
    }

    @Test
    void addProduct() {
        // GIVEN
        Product expected = p2;
        when(productRepo.addProduct(p2)).thenReturn(p2);
        when(idService.generateId()).thenReturn(p2.id());

        // WHEN
        Product actual = shopService.addProduct(p2);

        // THEN
        verify(productRepo).addProduct(p2);
        assertEquals(expected, actual);
    }

    @Test
    void deleteProduct() {
        // GIVEN
        List<Order> expected = new ArrayList<>(List.of(new Order(o1.id(), new ArrayList<>(List.of(p1)))));
        when(productRepo.getProduct(p2.id())).thenReturn(p2);
        when(orderRepo.listOrders()).thenReturn(bigOrderList);
        when(productRepo.deleteProduct(p2.id())).thenReturn(p2);

        // WHEN
        List<Order> actual = shopService.deleteProduct(p2.id());

        // THEN
        verify(orderRepo).listOrders();
        verify(productRepo).deleteProduct(p2.id());
        assertEquals(expected, actual);
    }
}