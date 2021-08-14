package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("select s.percentage from Sale s where s.product.id = ?2 and s.endDate > ?1")
    Integer isProductOnSale(Date now, Long id);
}
