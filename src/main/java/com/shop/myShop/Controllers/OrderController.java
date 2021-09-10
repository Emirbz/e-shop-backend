package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.*;
import com.shop.myShop.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("orders")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class OrderController {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SizeRepository sizeRepository;

    @PostMapping
    ResponseEntity<Order> addOrder(@RequestBody Order order) {
        AtomicReference<Double> price = new AtomicReference<>(0.0);
        Order o = new Order();
        o.setStatus(order.getStatus());
        User user = userRepository.save(order.getUser());
        o.setUser(user);
        order.getOrderItems().forEach(item -> {
            Product pr = productRepository.findById(item.getProduct().getId()).orElse(null);
            item.setProduct(pr);
            Size size = sizeRepository.findById(item.getSize().getId()).orElse(null);
            item.setSize(size);
            OrderItem orderItem = orderItemRepository.save(item);
            o.getOrderItems().add(orderItem);
            if (pr != null) {
                double p = pr.getPrice() * item.getQuantity();
                BigDecimal bd = new BigDecimal(p).setScale(2, RoundingMode.HALF_UP);
                price.updateAndGet(v -> v + bd.doubleValue());
            }
        });
        o.setTotalPrice(price.get());
        Order persisted = orderRepository.save(o);
        persisted.getOrderItems().forEach(item -> {
            item.setOrder(o);
        });
        orderRepository.flush();
        return ResponseEntity.ok(persisted);
    }

    @GetMapping
    ResponseEntity getOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PatchMapping("/{id}")
    ResponseEntity updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        Map<String, String> error = new HashMap<>();
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            return ResponseEntity.ok(order);
        } else {
            error.put("error", "Order not found");
            return ResponseEntity.badRequest().body(error);
        }
    }
}
