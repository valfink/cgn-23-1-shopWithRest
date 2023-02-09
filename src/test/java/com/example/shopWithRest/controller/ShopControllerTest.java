package com.example.shopWithRest.controller;

import com.example.shopWithRest.model.Order;
import com.example.shopWithRest.model.Product;
import com.example.shopWithRest.repository.OrderRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShopControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    OrderRepo orderRepo;

    @Test
    @DirtiesContext
    void listProducts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                [
                    {"id": 1, "name": "Apfel"},
                    {"id": 2, "name": "Banane"},
                    {"id": 3, "name": "Zitrone"},
                    {"id": 4, "name": "Mandarine"}
                ]
"""
    ));
    }

    @Test
    @DirtiesContext
    void getProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                    {"id" : 1,
                                     "name" : "Apfel"
                                    }              
                                            """));
    }

    @Test
    @DirtiesContext
    void addProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""" 
                        {
                        "name": "Test",
                        "id":10
                        }
                """))
        .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                 "name": "Test",
                  "id":10
                }
"""));


    }

    @Test
    @DirtiesContext
    void deleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void listOrders() throws Exception {
        orderRepo.addOrder(new Order("1", new ArrayList<>(List.of(new Product("1", "Banane")))));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                     [ 
                                     {"id": "1", "products": [{"id": 1, "name": "Banane"}]}
                                     ]"""));
                                  }

    @Test
    @DirtiesContext
    void getOrder() throws Exception {
        orderRepo.addOrder(new Order("1", new ArrayList<>(List.of(new Product("1", "Banane")))));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                    "id":"1",
                    "products":[
                        {"id":"1","name":"Banane"}
                    ]
                }
"""));
    }

    @Test
    @DirtiesContext
    void addOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/orders/8")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        [1,2] 
                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                "id": 8, "products": [{"id": 1, "name": "Apfel"},{"id": 2, "name": "Banane"}]
                }
"""));
    }

    @Test
    @DirtiesContext
    void deleteOrder() throws Exception {
        orderRepo.addOrder(new Order("1", new ArrayList<>(List.of(new Product("1", "Banane")))));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                {"id": "1",
                "products": [{"id": "1", "name": "Banane"}]
                }
                """));

    }
}