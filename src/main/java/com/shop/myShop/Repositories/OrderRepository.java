package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.user.phone = ?1")
    List<Order> getOrdersByPhoneNumber(String phone);
}
