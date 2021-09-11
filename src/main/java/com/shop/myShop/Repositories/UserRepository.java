package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email =?1")
    User findUserByEmail(String email);

    @Query("select u from User u where u.phone = ?1")
    User getUserByPhone(String phone);
}
