package com.shop.myShop.Repositories;

import com.shop.myShop.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where p.category.id = ?1 order by p.dateAdded Desc")
    Page<Product> getProductsByCategory(Long id, Pageable pageable);
}
