package com.shop.myShop.Controllers;

import com.shop.myShop.Entities.Order;
import com.shop.myShop.Entities.OrderItem;
import com.shop.myShop.Entities.Product;
import com.shop.myShop.Entities.User;
import com.shop.myShop.Repositories.OrderItemRepository;
import com.shop.myShop.Repositories.OrderRepository;
import com.shop.myShop.Repositories.ProductRepository;
import com.shop.myShop.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
}
