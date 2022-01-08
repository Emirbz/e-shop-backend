package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {

    @Query("select n from Nutrition n  where n.menu = ?1")
    Nutrition getByMenu(String menu);


}
